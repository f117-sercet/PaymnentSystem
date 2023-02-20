package com.payment.pay.mch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description： webMVC配置
 *
 *
 * @author: 段世超
 * @aate: Created in 2023/2/17 16:56
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApiResInterceptor apiResInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiResInterceptor);
    }
}
