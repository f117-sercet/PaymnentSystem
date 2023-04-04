package com.pay.payment.rqrs.refund;

import com.pay.payment.rqrs.AbstractMchAppRQ;
import lombok.Data;

/**
 * Description：查询退款单请求参数
 *
 * @author: 段世超
 * @aate: Created in 2023/4/4 17:18
 */
@Data
public class QueryRefundOrderRQ extends AbstractMchAppRQ {
    /** 商户退款单号 **/
    private String mchRefundNo;

    /** 支付系统退款订单号 **/
    private String refundOrderId;
}
