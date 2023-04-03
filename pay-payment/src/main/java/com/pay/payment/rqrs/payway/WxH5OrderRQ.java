package com.pay.payment.rqrs.payway;


import com.pay.pay.core.constants.CS;
import com.pay.payment.rqrs.payorder.CommonPayDataRQ;
import lombok.Data;

/*
 * 支付方式： WX_H5
 *
 */
@Data
public class WxH5OrderRQ extends CommonPayDataRQ {

    /** 构造函数 **/
    public WxH5OrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.WX_H5);
    }

}
