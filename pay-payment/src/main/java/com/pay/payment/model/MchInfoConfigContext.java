package com.pay.payment.model;

import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchInfo;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @create: : Created in 2023/4/3 10:14
 */
@Data
public class MchInfoConfigContext {

    /** 商户信息缓存 */
    private String mchNo;
    private Byte mchType;
    private MchInfo mchInfo;
    private Map<String, MchApp> appMap = new ConcurrentHashMap<>();

    /** 重置商户APP **/
    public void putMchApp(MchApp mchApp){
        appMap.put(mchApp.getAppId(), mchApp);
    }

    /** get商户APP **/
    public MchApp getMchApp(String appId){
        return appMap.get(appId);
    }
}
