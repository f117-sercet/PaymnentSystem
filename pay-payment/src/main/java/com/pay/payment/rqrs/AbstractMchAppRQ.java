package com.pay.payment.rqrs;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2023/3/16 11:39
 */
@Data
public class AbstractMchAppRQ extends AbstractRQ {
    /** 商户号 **/
    @NotBlank(message="商户号不能为空")
    private String mchNo;

    /** 商户应用ID **/
    @NotBlank(message="商户应用ID不能为空")
    private String appId;
}
