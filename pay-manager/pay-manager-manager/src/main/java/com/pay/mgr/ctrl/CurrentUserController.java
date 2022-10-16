package com.pay.mgr.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysEntitlement;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.core.utils.TreeDataBuilder;
import com.pay.pay.service.impl.SysEntitlementService;
import com.pay.pay.service.impl.SysUserAuthService;
import com.pay.pay.service.impl.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 当前登录者的信息相关接口
 *
 * @author: 段世超
 * @aate: Created in 2022/10/1 19:21
 */
@RestController
@RequestMapping("api/current")
public class CurrentUserController extends CommonCtrl {

    @Resource
    private SysEntitlementService  sysEntitlementService;

    @Resource
    private SysUserAuthService sysUserAuthService;
    @Resource
    private SysUserService sysUserService;

    @RequestMapping(value="/user", method = RequestMethod.GET)
    public ApiRes currentUserInfo() {

        // 当前用户信息
        JeeUserDetails jeeUserDetails = new JeeUserDetails();
        SysUser user = jeeUserDetails.getSysUser();

        //1. 当前用户所有权限ID集合
        List<String> entIdList = new ArrayList<>();
        jeeUserDetails.getAuthorities().stream().forEach(r->entIdList.add(r.getAuthority()));

        List<SysEntitlement> allMenuList = new ArrayList<>();    //所有菜单集合

        //2. 查询出用户所有菜单集合 (包含左侧显示菜单 和 其他类型菜单 )
        if(!entIdList.isEmpty()){
            allMenuList = sysEntitlementService.list(SysEntitlement.gw()
                    .in(SysEntitlement::getEntId, entIdList)
                    .in(SysEntitlement::getEntType, Arrays.asList(CS.ENT_TYPE.MENU_LEFT, CS.ENT_TYPE.MENU_OTHER))
                    .eq(SysEntitlement::getSysType, CS.SYS_TYPE.MGR)
                    .eq(SysEntitlement::getState, CS.PUB_USABLE));
        }

        //4. 转换为json树状结构
        JSONArray jsonArray = (JSONArray) JSON.toJSON(allMenuList);
        List<JSONObject> allMenuRouteTree = new TreeDataBuilder(jsonArray,
                "entId", "pid", "children", "entSort", true)
                .buildTreeObject();

        //1. 所有权限ID集合
        user.addExt("entIdList",entIdList);
        user.addExt("allMenuRouteTree",allMenuList);
        return ApiRes.ok(getCurrentUser().getSysUser());
    }

    /** 修改个人信息 */
    @RequestMapping(value="/user", method = RequestMethod.PUT)
    @MethodLog(remark = "修改信息")
    public ApiRes modifyCurrentUserInfo(){

         // 修改头像
        String avatarUrl = getAvatarUrl("avatarUrl");
        String realname = getValString("realname");
        Byte sex = getValByte("sex");
        SysUser updateRecord = new SysUser();
        updateRecord.setSysUserId(getCurrentUser().getSysUser().getSysUserId());

        if (StringUtils.isNotEmpty(avatarUrl)){

            updateRecord.setAvatarUrl(avatarUrl);
        }
        if(StringUtils.isNotEmpty(realname)){
            updateRecord.setRealname(realname);
        }
        if (sex != null) {
            updateRecord.setSex(sex);
        }
        sysUserService.updateById(updateRecord);

        // 保存到redis最新数据

        //保存redis最新数据
        JeeUserDetails currentUser = getCurrentUser();
        currentUser.setSysUser(sysUserService.getById(getCurrentUser().getSysUser().getSysUserId()));

        return ApiRes.ok();
    }

    private String getAvatarUrl(String avatarUrl) {

        return avatarUrl;
    }
}
