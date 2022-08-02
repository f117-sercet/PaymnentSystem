package com.pay.pay.core.exeception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * Description： security框架自定义异常
 *
 * @author: 段世超
 * @aate: Created in 2022/8/2 15:37
 */
@Getter
@Setter
public class JeepayAuthenticationException extends InternalAuthenticationServiceException {

    private BizException bizException;

    public JeepayAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JeepayAuthenticationException(String msg) {
        super(msg);
    }

    public static JeepayAuthenticationException build(String msg){
        return build(new BizException(msg));
    }

    public static JeepayAuthenticationException build(BizException ex){

        JeepayAuthenticationException result = new JeepayAuthenticationException(ex.getMessage());
        result.setBizException(ex);
        return result;
    }

}