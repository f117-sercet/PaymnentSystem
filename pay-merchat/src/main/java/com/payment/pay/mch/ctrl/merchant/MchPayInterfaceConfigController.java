package com.payment.pay.mch.ctrl.merchant;

import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.MchInfo;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.params.NormalMchParams;
import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.PayInterfaceConfigService;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.ctrl.anon.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *商户支付接口配置类
 *
 * @author: 段世超
 * @aate: Created in 2023/1/31 16:12
 */

@RestController
@RequestMapping("/api/mch/payConfigs")
public class MchPayInterfaceConfigController extends CommonCtrl {

    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;
    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private IMQSender mqSender;

    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_LIST')")
    @GetMapping
    public ApiRes list() {
        MchInfo mchInfo = mchInfoService.getById(getCurrentUser().getSysUser().getBelongInfoId());
        List<PayInterfaceDefine> list = payInterfaceConfigService.selectAllPayIfConfigListByAppId(getValStringRequired("appId"));

        for (PayInterfaceDefine define : list) {
            define.addExt("mchParams", mchInfo.getType() == CS.MCH_TYPE_NORMAL ? define.getNormalMchParams() : define.getIsvsubMchParams());
            define.setNormalMchParams(null);
            define.setIsvsubMchParams(null);
        }
        return ApiRes.ok(list);
    }

    /**
     * 数据脱敏
     *
     * @param appId
     * @param ifCode
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_VIEW')")
    @GetMapping("/{appId}/{ifCode}")
    public ApiRes getByMchNo(@PathVariable(value = "appId") String appId, @PathVariable(value = "ifCode") String ifCode) {
        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_MCH_APP, appId, ifCode);
        if (payInterfaceConfig != null) {
            // 费率转换为百分比数值
            if (payInterfaceConfig.getIfRate() != null) {
                payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().multiply(new BigDecimal("100")));
            }

            // 敏感数据脱敏
            if (StringUtils.isNotBlank(payInterfaceConfig.getIfParams())) {
                MchInfo mchInfo = mchInfoService.getById(getCurrentMchNo());

                // 普通商户的支付参数执行数据脱敏
                if (mchInfo.getType() == CS.MCH_TYPE_NORMAL) {
                    NormalMchParams mchParams = NormalMchParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());
                    if (mchParams != null) {
                        payInterfaceConfig.setIfParams(mchParams.deSenData());
                    }
                }
            }
        }
        return ApiRes.ok(payInterfaceConfig);
    }

    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_ADD')")
    @PostMapping
    @MethodLog(remark = "更新商户支付参数")
    public ApiRes saveOrUpdate() {
        String ifCode = getValStringRequired("ifCode");
        String infoId = getValStringRequired("infoId");
    }
}
