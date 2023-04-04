package com.pay.payment.rqrs.refund;

import com.pay.pay.core.entity.RefundOrder;
import com.pay.payment.rqrs.AbstractRS;
import org.springframework.beans.BeanUtils;

/**
 * 退款订单 响应参数
 *
 * @author: 段世超
 * @aate: Created in 2023/4/4 17:24
 */
public class RefundOrderRS extends AbstractRS {

    /** 支付系统退款订单号 **/
    private String refundOrderId;

    /** 商户发起的退款订单号 **/
    private String mchRefundNo;

    /** 订单支付金额 **/
    private Long payAmount;

    /** 申请退款金额 **/
    private Long refundAmount;

    /** 退款状态 **/
    private Byte state;

    /** 渠道退款单号   **/
    private String channelOrderNo;

    /** 渠道返回错误代码 **/
    private String errCode;

    /** 渠道返回错误信息 **/
    private String errMsg;


    public static RefundOrderRS buildByRefundOrder(RefundOrder refundOrder){

        if(refundOrder == null){
            return null;
        }

        RefundOrderRS result = new RefundOrderRS();
        BeanUtils.copyProperties(refundOrder, result);

        return result;
    }
}
