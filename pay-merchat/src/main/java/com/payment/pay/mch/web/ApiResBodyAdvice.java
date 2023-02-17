package com.payment.pay.mch.web;

import com.pay.pay.core.utils.ApiResBodyAdviceKit;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Description： 自定义springMVC返回数据格式
 *
 * @author: 段世超
 * @aate: Created in 2023/2/17 16:24
 */

@ControllerAdvice
public class ApiResBodyAdvice implements ResponseBodyAdvice {


    /**
     * 判断需要哪些需要拦截器
     * @param returnType the return type
     * @param converterType the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * 拦截返回数据处理
     * @return
     */

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        //处理扩展字段
        return ApiResBodyAdviceKit.beforeBodyWrite(body);
    }
}
