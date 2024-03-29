package com.payment.pay.mch.ctrl.sysuser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.entity.SysEntitlement;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.utils.TreeDataBuilder;
import com.pay.payMbg.service.impl.SysEntitlementService;
import com.payment.pay.mch.ctrl.CommonCtrl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    /** updateById */
    @PreAuthorize("hasAuthority( 'ENT_UR_ROLE_ENT_EDIT')")
    @MethodLog(remark = "更新资源权限")
    @RequestMapping(value="/{entId}", method = RequestMethod.PUT)
    public ApiRes updateById(@PathVariable("entId")  String entId){

        SysEntitlement queryObject = getObject(SysEntitlement.class);
        sysEntitlementService.update(queryObject, SysEntitlement.gw().eq(SysEntitlement::getEntId, entId).eq(SysEntitlement::getSysType, queryObject.getSysType()));
        return ApiRes.ok();
    }

    @PreAuthorize("hasAnyAuthority( 'ENT_UR_ROLE_ENT_LIST', 'ENT_UR_ROLE_DIST' )")
    @RequestMapping(value="/showTree", method = RequestMethod.GET)
      public ApiRes showTree(){

        List<SysEntitlement> list = sysEntitlementService.list(SysEntitlement.gw().eq(SysEntitlement::getSysType, getValStringRequired("sysType")));

        JSONArray jsonArray = (JSONArray) JSONArray.toJSON(list);
        List<JSONObject> leftMenuTree = new TreeDataBuilder(jsonArray,
                "entId", "pid", "children", "entSort", true)
                .buildTreeObject();

        return ApiRes.ok(leftMenuTree);

    }
}
