package com.pay.payment.rqrs.transfer;

import com.pay.payment.rqrs.AbstractMchAppRQ;
import lombok.Data;

/**
 * Description： 转装请求参数
 *
 * @author: 段世超
 * @aate: Created in 2023/4/7 14:49
 */
@Data
public class QueryTransferOrderRQ extends AbstractMchAppRQ {

    /** 商户转账单号 **/
    private String mchOrderNo;

    /** 支付系统转账单号 **/
    private String transferId;
}
