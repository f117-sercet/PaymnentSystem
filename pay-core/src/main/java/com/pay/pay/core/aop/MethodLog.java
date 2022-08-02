package com.pay.pay.core.aop;

import java.lang.annotation.*;

/**
 * Description： 方法级日志切面注解
 *
 * @author: 段世超
 * @aate: Created in 2022/8/1 11:13
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodLog {

    String remark()default "";
}
