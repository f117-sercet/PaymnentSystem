package com.payment.pay.mch.ctrl.anon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysEntitlement;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.core.utils.TreeDataBuilder;
import com.pay.pay.service.impl.SysEntitlementService;
import com.pay.pay.service.impl.SysUserAuthService;
import com.pay.pay.service.impl.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 当前登录者信息的相关接口
 *
 * @author: 段世超
 * @aate: Created in 2023/1/17 17:46
 */
@RestController
@RequestMapping("api/current")
public class CurrentUserController extends  CommonCtrl {

    @Resource
    private SysEntitlementService sysEntitlementService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysUserAuthService sysUserAuthService;

    public ApiRes currentUserInfo() {

        // 当前用户信息
        JeeUserDetails jeeUserDetails = getCurrentUser();
        SysUser user = jeeUserDetails.getSysUser();

        //1.当前用户所有权限的ID集合
        List<String> entIdList = new ArrayList<>();
        jeeUserDetails.getAuthorities().stream().forEach(r -> entIdList.add(r.getAuthority()));
        List<SysEntitlement> allMenuList = new ArrayList<>(); // 所有菜单集合

        //2.查询出用户所有菜单集合(包含左侧显示菜单 和 其他类型)
        if (!entIdList.isEmpty()) {
            allMenuList = sysEntitlementService.list(
                    SysEntitlement.gw()
                            .in(SysEntitlement::getEntId, entIdList)
                            .in(SysEntitlement::getEntType, Arrays.asList(CS.ENT_TYPE.MENU_LEFT, CS.ENT_TYPE.MENU_OTHER))
                            .eq(SysEntitlement::getSysType, CS.SYS_TYPE.MGR)
                            .eq(SysEntitlement::getState, CS.PUB_USABLE));
        }
        //4 转换为Json树状结构
        JSONArray jsonArray = (JSONArray) JSON.toJSON(allMenuList);
        List<JSONObject> allMenuRouteTree = new TreeDataBuilder(jsonArray,
                "entId", "pid", "children", "entSort", true)
                .buildTreeObject();

        //1. 所有权限ID集合
        user.addExt("entIdList", entIdList);
        user.addExt("allMenuRouteTree", allMenuRouteTree);
        return ApiRes.ok(getCurrentUser().getSysUser());
    }
    }
