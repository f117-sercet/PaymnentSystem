package com.pay.mgr.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 读取servletContext 上下文工具类
 *
 * @author: 段世超
 * @aate: Created in 2023/1/10 9:31
 */
@Service
public class ApplicationContextKit implements ServletContextAware, InitializingBean {

    private ServletContext servletContext ;

    @Override
    public void afterPropertiesSet() throws Exception {


    }

    @Override
    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;

    }

    /**
     * 仅在项目中启动完成，并且在req请求中使用！！
     * @param key
     * @return
     */
    public static Object getReqSession(String key){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        return request.getSession().getAttribute(key);
    }

    /**
     * 仅在项目启动完成，并且在req请求中使用！！
     * @param key
     * @return
     */
    public static void clearSession(String key){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        request.getSession().removeAttribute(key);

    }
}
