package com.pay.mgr.aop;

import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.beans.RequestKitBean;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysLog;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.service.impl.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Description： 方法级日志切面组件
 *
 * @author: 段世超
 * @aate: Created in 2022/9/9 18:28
 */
@Component
@Aspect
public class MethodLogAop {

    private static final Logger logger = LoggerFactory.getLogger(MethodLogAop.class);

    @Autowired
    private SysLogService sysLogService;

    @Autowired private RequestKitBean requestKitBean;

    /**
     * 异步处理线程池
     */
    private final static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    /**
     * 切点
     */
    @Pointcut("@annotation(com.jeequan.jeepay.core.aop.MethodLog)")
    public void methodCachePointcut() { }

    /**
     * 切面
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("methodCachePointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        final SysLog sysLog = new SysLog();
        //处理切面任务 发生异常将向外抛出 不记录日志
        Object result = point.proceed();

        try {
            // 基础日志信息
            setBaseLogInfo(point, sysLog, JeeUserDetails.getCurrentUserDetails());
            sysLog.setOptResInfo(JSONObject.toJSON(result).toString());
            scheduledThreadPool.execute(() -> sysLogService.save(sysLog));
        } catch (Exception e) {
            logger.error("methodLogError", e);
        }

        return result;
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 14:04
     * @describe: 记录异常操作请求信息
     */
    @AfterThrowing(pointcut = "methodCachePointcut()", throwing="e")
    public void doException(JoinPoint joinPoint, Throwable e) throws Exception{
        final SysLog sysLog = new SysLog();
        // 基础日志信息
        setBaseLogInfo(joinPoint, sysLog, JeeUserDetails.getCurrentUserDetails());
        sysLog.setOptResInfo(e instanceof BizException ? e.getMessage() : "请求异常");
        scheduledThreadPool.execute(() -> sysLogService.save(sysLog));
    }

    /**
     * 获取方法中的中文备注
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getAnnotationRemark(JoinPoint joinPoint) throws Exception {

        Signature sig = joinPoint.getSignature();
        Method m = joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(),  ((MethodSignature) sig).getParameterTypes());

        MethodLog methodCache = m.getAnnotation(MethodLog.class);
        if (methodCache != null) {
            return methodCache.remark();
        }
        return "";
    }

    /**
     * @author: pangxiaoyu
     * @date: 2021/6/7 14:12
     * @describe: 日志基本信息 公共方法
     */
    private void setBaseLogInfo(JoinPoint joinPoint, SysLog sysLog, JeeUserDetails userDetails) throws Exception {
        // 使用point.getArgs()可获取request，仅限于spring MVC参数包含request，改为通过contextHolder获取。
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //请求参数
        sysLog.setOptReqParam( requestKitBean.getReqParamJSON().toJSONString() );

        //注解备注
        sysLog.setMethodRemark(getAnnotationRemark(joinPoint));
        //包名 方法名
        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getThis().getClass().getName();
        if (packageName.indexOf("$$EnhancerByCGLIB$$") > -1 || packageName.indexOf("$$EnhancerBySpringCGLIB$$") > -1) { // 如果是CGLIB动态生成的类
            packageName = packageName.substring(0, packageName.indexOf("$$"));
        }
        sysLog.setMethodName(packageName + "." + methodName);
        sysLog.setReqUrl(request.getRequestURL().toString());
        sysLog.setUserIp(requestKitBean.getClientIp());
        sysLog.setCreatedAt(new Date());
        sysLog.setSysType(CS.SYS_TYPE.MGR);

        if (userDetails != null) {
            sysLog.setUserId(JeeUserDetails.getCurrentUserDetails().getSysUser().getSysUserId());
            sysLog.setUserName(JeeUserDetails.getCurrentUserDetails().getSysUser().getRealname());
            sysLog.setSysType(JeeUserDetails.getCurrentUserDetails().getSysUser().getSysType());
        }
    }

}

