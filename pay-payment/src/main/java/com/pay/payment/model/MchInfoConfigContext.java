package com.pay.payment.model;

import com.pay.pay.core.entity.MchApp;
import com.pay.pay.core.entity.MchInfo;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商户配置信息
 * 放置到内存， 避免多次查询操作
 *
 * @author: 段世超
 * @aate: Created in 2023/11/14 11:21
 */
@Data
public class MchInfoConfigContext {

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
