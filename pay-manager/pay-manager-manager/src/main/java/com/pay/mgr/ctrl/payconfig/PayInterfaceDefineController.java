package com.pay.mgr.ctrl.payconfig;

import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.PayInterfaceDefine;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.PayInterfaceConfigService;
import com.pay.pay.service.impl.PayInterfaceDefineService;
import com.pay.pay.service.impl.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description：支付接口定义管理类
 *
 * @author: 段世超
 * @aate: Created in 2022/12/3 20:58
 */
@RestController
@RequestMapping("api/payIfDefines")
public class PayInterfaceDefineController extends CommonCtrl {

    @Autowired
    private PayInterfaceDefineService payInterfaceDefineService;
    @Autowired private PayOrderService payOrderService;
    @Autowired private PayInterfaceConfigService payInterfaceConfigService;

    /**
     * 列表
     * @return
     */
    @PreAuthorize("hasAuthority('ENT_PC_IF_DEFINE_LIST')")
    @GetMapping
    public ApiRes list() {

        List<PayInterfaceDefine> list = payInterfaceDefineService.list(PayInterfaceDefine.gw()
                .orderByAsc(PayInterfaceDefine::getCreatedAt)
        );
        return ApiRes.ok(list);
    }
}
