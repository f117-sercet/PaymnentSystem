package com.payment.pay.mch.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Description： 上下文配置
 *
 * @author: 段世超
 * @aate: Created in 2023/2/17 16:53
 */
@Service
public class ApplicationContextKit implements ServletContextAware, InitializingBean {

    private ServletContext servletContext;
    @Override
    public void afterPropertiesSet() throws Exception {



    }

    @Override
    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;

    }
}
