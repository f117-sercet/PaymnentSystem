package com.payment.pay.mch.ctrl.sysuser;

import com.pay.pay.core.entity.SysEntitlement;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.service.impl.SysEntitlementService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 权限 菜单 管理
 *
 * @author: 段世超
 * @aate: Created in 2023/3/6 17:53
 */
@RestController
@RequestMapping("api/sysEnts")
public class SysEntController extends CommonCtrl {
    @Resource
    SysEntitlementService sysEntitlementService;

    /** getOne */
    @PreAuthorize("hasAnyAuthority( 'ENT_UR_ROLE_ENT_LIST' )")
    @RequestMapping(value="/bySysType", method = RequestMethod.GET)
    public ApiRes bySystem() {

        return ApiRes.ok(sysEntitlementService.getOne(SysEntitlement.gw()
                .eq(SysEntitlement::getEntId, getValStringRequired("entId"))
                .eq(SysEntitlement::getSysType, getValStringRequired("sysType")))
        );
    }
}
