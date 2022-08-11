package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.service.mapper.PayInterfaceConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2022/8/8 9:07
 */
@Service
public class PayInterfaceConfigService extends ServiceImpl<PayInterfaceConfigMapper, PayInterfaceConfig> {
    @Autowired
    private PayInterfaceDefineService payInterfaceDefineService;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private MchAppService mchAppService;

    /**
     * 根据账户类型，账户号，接口类型，获取支付参数配置
     * @param infoType
     * @param infoId
     * @param ifCode
     * @return
     */
    public PayInterfaceConfig getByInfoIdAndIfCode(Byte infoType, String infoId, String ifCode){

        return getOne(PayInterfaceConfig.gw()
                .eq(PayInterfaceConfig::getInfoType,infoType)
                .eq(PayInterfaceConfig::getInfoId,infoId)
                .eq(PayInterfaceConfig::getIfCode,ifCode));
    }

    /**
     * 根据 账户类型，账户号，获取支付配置参数
     * @param infoType
     * @param infoId
     * @return
     */
    public List<PayInterfaceDefine> selectAllPayIfConfigListByIsvNo(Byte infoType, String infoId){

        // 支付定义列表
        LambdaQueryWrapper<PayInterfaceDefine> queryWrapper = PayInterfaceDefine.gw();
        queryWrapper.eq(PayInterfaceDefine::getState, CS.YES);
        queryWrapper.eq(PayInterfaceDefine::getIsIsvMode, CS.YES); // 支持服务商模式

        List<PayInterfaceDefine> defineList = payInterfaceDefineService.list(queryWrapper);

        // 支付参数列表
        LambdaQueryWrapper<PayInterfaceConfig> wrapper = PayInterfaceConfig.gw();
        wrapper.eq(PayInterfaceConfig::getInfoId, infoId);
        wrapper.eq(PayInterfaceConfig::getInfoType, infoType);
        List<PayInterfaceConfig> configList = this.list(wrapper);

        for (PayInterfaceDefine define : defineList) {
            for (PayInterfaceConfig config : configList) {
                if (define.getIfCode().equals(config.getIfCode())) {
                    define.addExt("ifConfigState", config.getState()); // 配置状态
                }
            }
        }
        return defineList;
    }

    }
