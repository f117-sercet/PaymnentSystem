package com.pay.mgr.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Description： webmvc控制
 *
 * @author: 段世超
 * @aate: Created in 2023/1/10 11:05
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Resource
    private ApiResInterceptor apiResInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiResInterceptor);
    }
}
