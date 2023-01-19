package com.payment.pay.mch.ctrl.anon;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.cache.ITokenService;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysEntitlement;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.exeception.BizException;
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

    /**
     * 修改个人信息
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @MethodLog(remark = "修改信息")
    public ApiRes modifyCurrentUserInfo(){

        // 修改头像
        String avatarUrl = getValString("avatarUrl");
        String realname = getValString("realname");
        Byte sex = getValByte("sex");
        SysUser updateRecord = new SysUser();
         updateRecord.setSysUserId(getCurrentUser().getSysUser().getSysUserId());

         if (StringUtils.isNotBlank(realname)){

             updateRecord.setAvatarUrl(avatarUrl);
         }
         if (StringUtils.isNotEmpty(realname)){

             updateRecord.setRealname(realname);
         }
         if (StringUtils.isNotBlank(realname)){

             if (sex !=null){
                 updateRecord.setSex(sex);
             }
             sysUserService.updateById(updateRecord);
         }

         // 保存Redis最新数据
        JeeUserDetails currentUser = getCurrentUser();
         currentUser.setSysUser(sysUserService.getById(getCurrentUser().getSysUser().getSysUserId()));
        ITokenService.refData(currentUser);

        return ApiRes.ok();
    }

    /**
     * mdifyPwd
     * @return
     * @throws BizException
     */
    @MethodLog
    @RequestMapping(value = "modifyPwd",method = RequestMethod.PUT)
    public ApiRes modifyPwd() throws BizException{

        Long opSysUserId = getValLongRequired("recordId");   //操作员ID

        // 更改密码，验证用户当前信息
        String currentUserPwd = Base64.decodeStr(getValStringRequired("originalPwd"));
        // 验证当前密码是否正确
        if (sysUserAuthService.validateCurrentUserPwd(currentUserPwd)) {
            throw new BizException("密码验证失败！");
        }
        String opUserPwd = Base64.decodeStr(getValStringRequired("confirmPwd"));
// 验证原密码与新密码是否相同
        if (opUserPwd.equals(currentUserPwd)) {
            throw new BizException("新密码与原密码不能相同！");
        }

        sysUserAuthService.resetAuthInfo(opSysUserId, null, null, opUserPwd, CS.SYS_TYPE.MCH);
        //调用登出接口
        return logout();
    }
    /** 登出 */
    @MethodLog(remark = "退出")
    @RequestMapping(value="logout", method = RequestMethod.POST)
    public ApiRes logout() throws BizException{

        ITokenService.removeIToken(getCurrentUser().getCacheKey(), getCurrentUser().getSysUser().getSysUserId());
        return ApiRes.ok();
    }

    }
