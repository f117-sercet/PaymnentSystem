package com.pay.mgr.ctrl.isv;

import com.pay.components.mq.vender.IMQSender;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.PayInterfaceConfig;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.params.IsvParams;
import com.pay.pay.service.impl.PayInterfaceConfigService;
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
 * @aate: Created in 2022/9/30 18:44
 */
@RestController
@RequestMapping("/api/isv/payConfigs")
public class IsvPayInterfaceConfigController extends CommonCtrl {

    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;
    @Resource
    private IMQSender mqSender;


    /**
     * @Author: 段世超
     * @Description: 查询服务商支付接口配置列表
     * @Date: 16:45 2021/4/27
     */
    @PreAuthorize("hasAuthority('ENT_ISV_PAY_CONFIG_LIST')")
    @GetMapping
    public ApiRes list() {

        List<PayInterfaceDefine> list = payInterfaceConfigService.selectAllPayIfConfigListByIsvNo(CS.INFO_TYPE_ISV, ("isvNo"),getValStringRequired("isvNo"));
        return ApiRes.ok(list);
    }

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

    /***
     * 服务商支付接口参数配置
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_ISV_PAY_CONFIG_ADD')")
    @PostMapping
    @MethodLog(remark = "更新服务商支付参数")
    public ApiRes saveOrUpdate(){

        String infoId = getValStringRequired("infoId");
        String ifCode = getValStringRequired("ifCode");

        PayInterfaceConfig payInterfaceConfig = getObject(PayInterfaceConfig.class);
        payInterfaceConfig.setInfoType(CS.INFO_TYPE_ISV);

        // 存入真实费率
        if (payInterfaceConfig.getIfRate() != null) {
            payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP));
        }
        //添加更新者信息
        Long userId = getCurrentUser().getSysUser().getSysUserId();
        String realName = getCurrentUser().getSysUser().getRealname();
        payInterfaceConfig.setUpdatedUid(userId);
        payInterfaceConfig.setUpdatedBy(realName);

        //根据 服务商号、接口类型 获取商户参数配置
        PayInterfaceConfig dbRecoed = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_ISV, infoId, ifCode);
        //若配置存在，为saveOrUpdate添加ID，第一次配置添加创建者
        return ApiRes.ok();
    }

}
