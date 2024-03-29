package com.pay.payMbg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.IsvInfo;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.exeception.BizException;
import com.pay.payMbg.service.mapper.IsvInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description： 服务商户信息列表 服务实现类
 *
 * @author: 段世超
 * @aate: Created in 2022/8/5 14:36
 */
@Service
public class IsvInfoService extends ServiceImpl<IsvInfoMapper, IsvInfo> {

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private IsvInfoService isvInfoService;

    @Autowired
    private PayInterfaceConfigService payInterfaceConfigService;

    @Transactional
    public void removeByIsvNo(String isvNo) {
        //0.当前服务商是否存在
        IsvInfo isvInfo = isvInfoService.getById(isvNo);
        if (isvInfo == null) {

            throw new BizException("该服务商不存在");
        }

        // 1.查询当前服务商下受否存在商户
        int mchCount = mchInfoService.count(MchInfo.gw().eq(MchInfo::getMchNo, isvNo).eq(MchInfo::getType, CS.MCH_TYPE_ISVSUB));

        if (mchCount > 0) {

            throw new BizException("该服务商下存在商户，不可删除");
        }
        // 2.删除当前服务商支付接口配置参数
        payInterfaceConfigService.remove(PayInterfaceConfig.gw()
                .eq(PayInterfaceConfig::getInfoId, isvNo)
                .eq(PayInterfaceConfig::getInfoType, CS.INFO_TYPE_ISV)
        );

        // 3.删除该服务商
        boolean remove = isvInfoService.removeById(isvInfo);
        if (!remove) {
            throw new BizException("删除服务商失败");
        }
    }
}
