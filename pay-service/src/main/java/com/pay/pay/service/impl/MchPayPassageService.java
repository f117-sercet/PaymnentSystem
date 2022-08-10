package com.pay.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.service.mapper.MchPayPassageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  商户支付通道表 服务实现类
 *
 * @author: 段世超
 * @aate: Created in 2022/8/8 9:39
 */
@Service
public class MchPayPassageService extends ServiceImpl<MchPayPassageMapper, MchPayPassage> {

    @Autowired
    private PayInterfaceDefineService payInterfaceDefineService;

    /**
     * 根据支付方式查询可用的支付接口列表
     * @param wayCode
     * @param appId
     * @param infoType
     * @param mchType
     * @return
     */
    public List<JSONObject> selectAvailablePayInterfaceList(String wayCode, String appId, Byte infoType, Byte mchType){

        Map params = new HashMap();
        params.put("wayCode", wayCode);
        params.put("appId", appId);
        params.put("infoType", infoType);
        params.put("mchType", mchType);
        List<JSONObject> list = baseMapper.selectAvailAblePayInterfaceList(params);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        // 添加通道状态
        for (JSONObject object : list) {
            MchPayPassage payPassage = baseMapper.selectOne(MchPayPassage.gw()
                    .eq(MchPayPassage::getAppId, appId)
                    .eq(MchPayPassage::getWayCode, wayCode)
                    .eq(MchPayPassage::getIfCode, object.getString("ifCode"))
            );
            if (payPassage != null) {
                object.put("passageId", payPassage.getId());
                if (payPassage.getRate() != null) {
                    object.put("rate", payPassage.getRate().multiply(new BigDecimal("100")));
                }
                object.put("state", payPassage.getState());
            }
            if(object.getBigDecimal("ifRate") != null) {
                object.put("ifRate", object.getBigDecimal("ifRate").multiply(new BigDecimal("100")));
            }
        }
        return list;
    }



}
