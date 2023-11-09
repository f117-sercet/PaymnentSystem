package com.payment.pay.mch.ctrl.isv;

import com.pay.components.mq.model.ResetIsvMchAppInfoConfigMQ;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.params.IsvParams;
import com.pay.pay.core.utils.StringKit;
import com.pay.payMbg.service.impl.PayInterfaceConfigService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description： 服务商支付接口管理类
 *
 * @author: 段世超
 * @aate: Created in 2023/2/24 10:26
 */
@RestController
@RequestMapping("/api/isv/payConfigs")
public class IsvPayInterfaceConfigController extends CommonCtrl {

    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;
    @Resource
    private IMQSender mqSender;

    public ApiRes list() {

        List<PayInterfaceDefine> list = payInterfaceConfigService.selectAllPayIfConfigListByIsvNo(CS.INFO_TYPE_ISV, getValStringRequired("isvNo"));
        return ApiRes.ok(list);
    }

    /**
     * 根据服务商号码，接口类型，获取商户参数配置
     *
     * @param isvNo
     * @param ifCode
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ISV_PAY_CONFIG_VIEW')")
    @GetMapping("/{isvNo}/{ifCode}")
    public ApiRes getByMchNo(@PathVariable(value = "isvNo") String isvNo, @PathVariable(value = "ifCode") String ifCode) {

        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_ISV, isvNo, ifCode);
        if (payInterfaceConfig != null) {
            if (payInterfaceConfig.getIfRate() != null) {
                payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().multiply(new BigDecimal("100")));
            }
            if (StringUtils.isNotBlank(payInterfaceConfig.getIfParams())) {
                IsvParams isvParams = IsvParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());
                if (isvParams != null) {
                    payInterfaceConfig.setIfParams(isvParams.deSenData());
                }
            }
        }
        return ApiRes.ok(payInterfaceConfig);

    }

    /**
     * 服务商支付接口参数配置
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ISV_PAY_CONFIG_ADD')")
    @PostMapping
    @MethodLog(remark = "更新服务商支付参数")
    public ApiRes saveOrUpdate() {

        String infoId = getValStringRequired("infoId");
        String ifCode = getValStringRequired("ifCode");

        PayInterfaceConfig payInterfaceConfig = getObject(PayInterfaceConfig.class);
        payInterfaceConfig.setInfoType(CS.INFO_TYPE_ISV);

        // 存入真实费率
        if (payInterfaceConfig.getIfRate() != null) {
            payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP));

        }

        //添加更新者信息
        Long sysUserId = getCurrentUser().getSysUser().getSysUserId();
        String realName = getCurrentUser().getSysUser().getRealname();
        payInterfaceConfig.setUpdatedUid(sysUserId);
        payInterfaceConfig.setUpdatedBy(realName);

        //根据 服务商号、接口类型 获取商户参数配置
        PayInterfaceConfig dbRecoed = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_ISV, infoId, ifCode);

        //若配置存在，为saveOrUpdate添加ID，第一次配置添加创建者
        if (dbRecoed != null) {
            payInterfaceConfig.setId(dbRecoed.getId());

            // 合并支付参数
            payInterfaceConfig.setIfParams(StringKit.marge(dbRecoed.getIfParams(), payInterfaceConfig.getIfParams()));
        } else {
            payInterfaceConfig.setCreatedUid(sysUserId);
            payInterfaceConfig.setCreatedBy(realName);
        }

        boolean result = payInterfaceConfigService.saveOrUpdate(payInterfaceConfig);
        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYSTEM_ERROR, "配置失败");
        }

        // 推送mq到目前节点进行数据更新
        mqSender.send(ResetIsvMchAppInfoConfigMQ.build(ResetIsvMchAppInfoConfigMQ.RESET_TYPE_ISV_INFO, infoId, null, null));
        return ApiRes.ok();
    }
}