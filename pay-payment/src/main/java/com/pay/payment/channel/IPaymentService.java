package com.pay.payment.channel;

import com.pay.pay.core.entity.PayOrder;
import com.pay.payment.model.MchAppConfigContext;
import com.pay.payment.rqrs.AbstractRS;

/**
 * 调起上游渠道侧支付接口
 *
 * @author: 段世超
 * @aate: Created in 2023/11/15 17:51
 */
public interface IPaymentService {
    /** 获取到接口code **/
    String getIfCode();

    /** 是否支持该支付方式 */
    boolean isSupport(String wayCode);

    /** 前置检查如参数等信息是否符合要求， 返回错误信息或直接抛出异常即可  */
    String preCheck(UnifiedOrderRQ bizRQ, PayOrder payOrder);



    /** 调起支付接口，并响应数据；  内部处理普通商户和服务商模式  **/
    AbstractRS pay(UnifiedOrderRQ bizRQ, PayOrder payOrder, MchAppConfigContext mchAppConfigContext) throws Exception;


}
