package com.pay.payment.model;

import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.model.params.IsvsubMchParams;
import com.pay.pay.core.model.params.NormalMchParams;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户应用支付参数信息
 * 放置到内存，避免多次查询操作
 *
 * @author: 段世超
 * @aate: Created in 2023/3/30 15:02
 */
@Data
public class MchAppConfigContext {

    /** 商户信息缓存 */
    private String mchNo;
    private String appId;
    private Byte mchType;
    private MchInfo mchInfo;
    private MchApp mchApp;

    /** 商户支付配置信息缓存,  <接口代码, 支付参数>  */
    private Map<String, NormalMchParams> normalMchParamsMap = new HashMap<>();
    private Map<String, IsvsubMchParams> isvsubMchParamsMap = new HashMap<>();

    /** 放置所属服务商的信息 **/
    private IsvConfigContext isvConfigContext;

    /** 缓存 Paypal 对象 **/
    private PaypalWrapper paypalWrapper;

    /** 缓存支付宝client 对象 **/
    private AlipayClientWrapper alipayClientWrapper;

    /** 缓存 wxServiceWrapper 对象 **/
    private WxServiceWrapper wxServiceWrapper;
}
