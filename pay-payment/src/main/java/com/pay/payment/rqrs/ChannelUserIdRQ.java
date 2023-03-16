package com.pay.payment.rqrs;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 商户获取渠道用户ID 请求参数对象
 *
 * @author: 段世超
 * @aate: Created in 2023/3/16 11:43
 */
@Data
public class ChannelUserIdRQ extends AbstractRQ {

    /** 接口代码,  AUTO表示：自动获取 **/
    @NotBlank(message="接口代码不能为空")
    private String ifCode;

    /** 商户扩展参数，将原样返回 **/
    private String extParam;

    /** 回调地址 **/
    @NotBlank(message="回调地址不能为空")
    private String redirectUrl;
}
