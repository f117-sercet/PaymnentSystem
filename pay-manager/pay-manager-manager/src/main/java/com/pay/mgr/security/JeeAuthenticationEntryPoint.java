package com.pay.mgr.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 用户身份认证失败处理类别
 *
 * @author: 段世超
 * @aate: Created in 2023/1/10 9:14
 */
@Component
public class JeeAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable  {


    @Override
    public void commence(HttpServletRequest  request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");


        //返回json形式的错误信息
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.getWriter().println("{\"code\":1001, \"msg\":\"Unauthorized\"}");

        response.getWriter().flush();

    }
}

