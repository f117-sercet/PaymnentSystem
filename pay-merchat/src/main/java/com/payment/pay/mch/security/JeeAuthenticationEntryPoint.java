package com.payment.pay.mch.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Description： 用户身份认证失败处理类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/21 18:39
 */
@Component
public class JeeAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

        //返回json形式的错误信息
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json");
//        response.getWriter().println("{\"code\":1001, \"msg\":\"Unauthorized\"}");
        response.getWriter().flush();

    }
}
