package com.pay.mgr.aop;

import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.beans.RequestKitBean;
import com.pay.pay.core.entity.SysLog;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.service.impl.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private SysLogService sysLogService;

    @Resource
    private RequestKitBean requestKitBean;


    /***
     * 异步处理线程池
     */
    private final static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    @Pointcut("@annotation(com.pay.pay.core.aop.MethodLog)")
    public void  methodCachePointcut(){}


    /***
     * 切面
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("methodCachePointcut()")
    public  Object around(ProceedingJoinPoint point) throws Throwable{

         final SysLog sysLog = new SysLog();

             /***处理切面任务 发生异常将向外抛出，不记录日志***/

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

    private void setBaseLogInfo(ProceedingJoinPoint point, SysLog sysLog, JeeUserDetails currentUserDetails) {
    }

}


