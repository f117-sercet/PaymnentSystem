package com.pay.payment.model;

import com.pay.pay.core.entity.IsvInfo;
import com.pay.pay.core.model.params.IsvParams;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Isv支付参数信息 放置到内存， 避免多次查询操作
 *
 * @author: 段世超
 * @aate: Created in 2023/3/23 8:57
 */
@Data
public class IsvConfigContext {

    /** isv信息缓存 */
    private String isvNo;
    private IsvInfo isvInfo;

    /** 商户支付配置信息缓存 */
    private Map<String, IsvParams> isvParamsMap = new HashMap<>();


    /** 缓存支付宝client 对象 **/
    private AlipayClientWrapper alipayClientWrapper;

    /** 缓存 wxServiceWrapper 对象 **/
    private WxServiceWrapper wxServiceWrapper;
}
