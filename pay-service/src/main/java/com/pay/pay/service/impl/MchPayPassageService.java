package com.pay.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchPayPassage;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.service.mapper.MchPayPassageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateBatchSelf(List<MchPayPassage> mchPayPassageList,String mchhNo){
        for (MchPayPassage payPassage : mchPayPassageList) {
            if (payPassage.getState() == CS.NO && payPassage.getId() == null){

                continue;
            }
            if (StrUtil.isNotBlank(mchhNo)){
                payPassage.setMchNo(mchhNo);
            }
            if (payPassage.getRate()!= null){
                payPassage.setRate(payPassage.getRate().divide(new BigDecimal("100"),6,BigDecimal.ROUND_HALF_UP));
            }
            if (!saveOrUpdate(payPassage)){
                throw new BizException("操作失败");
            }
        }
    }
         /*******根据应用ID和支付方式，查询出商户可用的支付接口*******/
         public MchPayPassage findMchPayPassage(String mcNo,String appId,String wayCode){

             List<MchPayPassage> list = list(MchPayPassage.gw()
                     .eq(MchPayPassage::getMchNo,mcNo)
                     .eq(MchPayPassage::getAppId,appId)
                     .eq(MchPayPassage::getState,CS.YES)
                     .eq(MchPayPassage::getWayCode,wayCode)
             );

             if (list.isEmpty()){
                 return null;
             }else {
                 //返回一个可用通道
                 HashMap<String,MchPayPassage> mchPayPassageHashMap = new HashMap<>();
                 for (MchPayPassage mchPayPassage : list) {
                     mchPayPassageHashMap.put(mchPayPassage.getIfCode(),mchPayPassage);
                 }
                 //查询ifCode所有接口
                 PayInterfaceDefine interfaceDefine = payInterfaceDefineService
                         .getOne(PayInterfaceDefine.gw()
                                 .select(PayInterfaceDefine::getIfCode,PayInterfaceDefine::getState)
                                 .eq(PayInterfaceDefine::getState,CS.YES)
                                 .in(PayInterfaceDefine::getIfCode,mchPayPassageHashMap.keySet(),false)
                         );
                 if (interfaceDefine!= null){
                     return mchPayPassageHashMap.get(interfaceDefine.getIfCode());
                 }
             }
             return null;
         }

}
