package com.pay.mgr.ctrl.isv;

import com.pay.components.mq.vender.IMQSender;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.PayInterfaceConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
}
