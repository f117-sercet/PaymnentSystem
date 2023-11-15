package com.pay.payment.service;

import com.pay.payMbg.service.impl.IsvInfoService;
import com.pay.payMbg.service.impl.MchAppService;
import com.pay.payMbg.service.impl.MchInfoService;
import com.pay.payMbg.service.impl.PayInterfaceConfigService;
import com.pay.payment.model.IsvConfigContext;
import com.pay.payment.model.MchAppConfigContext;
import com.pay.payment.model.MchInfoConfigContext;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description： 商户/服务商 配置信息上下文服务
 *
 * @author: 段世超
 * @aate: Created in 2023/11/15 17:55
 */
public class ConfigContextService {

    /** <商户ID, 商户配置项>  **/
    private static final Map<String, MchInfoConfigContext> mchInfoConfigContextMap = new ConcurrentHashMap<>();

    /** <应用ID, 商户配置上下文>  **/
    private static final Map<String, MchAppConfigContext> mchAppConfigContextMap = new ConcurrentHashMap<>();

    /** <服务商号, 服务商配置上下文>  **/
    private static final Map<String, IsvConfigContext> isvConfigContextMap = new ConcurrentHashMap<>();

    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private IsvInfoService isvInfoService;
    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;



    // 获取商户配置信息
    public MchInfoConfigContext getMchInfoConfigContext(String mchNo){

        MchInfoConfigContext mchInfoConfigContext = mchInfoConfigContextMap.get(mchNo);

        //无此数据， 需要初始化
        if(mchInfoConfigContext == null){
            initMchInfoConfigContext(mchNo);
        }

        return mchInfoConfigContextMap.get(mchNo);
    }

}
