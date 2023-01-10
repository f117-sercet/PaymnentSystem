package com.payment.pay.mch.aop;

import com.pay.pay.core.beans.RequestKitBean;
import com.pay.pay.service.impl.SysLogService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Description： 方法日志切面组件
 *
 * @author: 段世超
 * @aate: Created in 2023/1/10 11:41
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


}
