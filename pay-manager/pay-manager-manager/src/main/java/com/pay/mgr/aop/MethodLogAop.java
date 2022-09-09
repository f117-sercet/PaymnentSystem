package com.pay.mgr.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

}
