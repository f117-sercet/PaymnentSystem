package com.pay.mgr.ctrl.sysuser;

import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.mgr.service.AuthService;
import com.pay.pay.core.aop.MethodLog;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.model.ApiRes;
import com.pay.payMbg.service.impl.SysUserAuthService;
import com.pay.payMbg.service.impl.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * Description： 操作员列表
 *
 * @author: 段世超
 * @aate: Created in 2023/1/9 11:38
 */
@RestController
@RequestMapping("api/sysUsers")
public class SysUserController extends CommonCtrl {

    @Autowired
    private SysUserService sysUserService;
    @Autowired private SysUserAuthService sysUserAuthService;
    @Autowired private AuthService authService;


    @PreAuthorize("hasAuthority( 'ENT_UR_USER_LIST' )")
    @RequestMapping(value="", method = RequestMethod.GET)
    public ApiRes list() {

        SysUser queryObject = getObject(SysUser.class);

        LambdaQueryWrapper<SysUser> condition = SysUser.gw();
        condition.eq(SysUser::getSysType, CS.SYS_TYPE.MGR);

        if(StringUtils.isNotEmpty(queryObject.getRealname())){
            condition.like(SysUser::getRealname, queryObject.getRealname());
        }

        if(queryObject.getSysUserId() != null){
            condition.eq(SysUser::getSysUserId, queryObject.getSysUserId());
        }

        condition.orderByDesc(SysUser::getCreatedAt); //时间： 降序

        IPage<SysUser> pages = sysUserService.page(getIPage(), condition);

        return ApiRes.page(pages);
    }

    /** detail */
    @PreAuthorize("hasAuthority( 'ENT_UR_USER_EDIT' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("recordId") Integer recordId) {
        return ApiRes.ok(sysUserService.getById(recordId));
    }

    @PreAuthorize("hasAuthority( 'ENT_UR_USER_ADD' )")
    @MethodLog(remark = "添加管理员")
    @RequestMapping(value="", method = RequestMethod.POST)
    public ApiRes add() {
        SysUser sysUser = getObject(SysUser.class);
        sysUser.setBelongInfoId("0");

        sysUserService.addSysUser(sysUser, CS.SYS_TYPE.MGR);
        return ApiRes.ok();
    }

    /** 修改操作员 登录认证信息 */
//	@RequestMapping(value="/modifyPwd", method = RequestMethod.PUT)
//	@MethodLog(remark = "修改操作员密码")
    public ApiRes authInfo() {

        Long opSysUserId = getValLongRequired("recordId");   //操作员ID

        //更改密码， 验证当前用户信息
        String currentUserPwd = getValStringRequired("originalPwd"); //当前用户登录密码
        //验证当前密码是否正确
        if(!sysUserAuthService.validateCurrentUserPwd(currentUserPwd)){
            throw new BizException("原密码验证失败！");
        }

        String opUserPwd = getValStringRequired("confirmPwd");

        // 验证原密码与新密码是否相同
        if (opUserPwd.equals(currentUserPwd)) {
            throw new BizException("新密码与原密码相同！");
        }

        sysUserAuthService.resetAuthInfo(opSysUserId, null, null, opUserPwd, CS.SYS_TYPE.MGR);
        return ApiRes.ok();
    }

    /** update */
    @PreAuthorize("hasAuthority( 'ENT_UR_USER_EDIT' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.PUT)
    @MethodLog(remark = "修改操作员信息")
    public ApiRes update(@PathVariable("recordId") Long recordId) {
        SysUser sysUser = getObject(SysUser.class);
        sysUser.setSysUserId(recordId);
        //判断是否自己禁用自己
        if(recordId.equals(getCurrentUser().getSysUser().getSysUserId()) && sysUser.getState() != null && sysUser.getState() == CS.PUB_DISABLE){
            throw new BizException("系统不允许禁用当前登陆用户！");
        }
        //判断是否重置密码
        Boolean resetPass = getReqParamJSON().getBoolean("resetPass");
        if (resetPass != null && resetPass) {
            String updatePwd = getReqParamJSON().getBoolean("defaultPass") == false ? Base64.decodeStr(getValStringRequired("confirmPwd")) : CS.DEFAULT_PWD;
            sysUserAuthService.resetAuthInfo(recordId, null, null, updatePwd, CS.SYS_TYPE.MGR);
            // 删除用户redis缓存信息
            authService.delAuthentication(Arrays.asList(recordId));
        }

        sysUserService.updateSysUser(sysUser);

        //如果用户被禁用，需要更新redis数据
        if(sysUser.getState() != null && sysUser.getState() == CS.PUB_DISABLE){
            authService.refAuthentication(Arrays.asList(recordId));
        }
        return ApiRes.ok();
    }

    /** delete */
    @PreAuthorize("hasAuthority( 'ENT_UR_USER_DELETE' )")
    @RequestMapping(value="/{recordId}", method = RequestMethod.DELETE)
    @MethodLog(remark = "删除操作员信息")
    public ApiRes delete(@PathVariable("recordId") Long recordId) {
        //查询该操作员信息
        SysUser sysUser = sysUserService.getById(recordId);
        if (sysUser == null) {
            throw new BizException("该操作员不存在！");
        }

        //判断是否自己删除自己
        if(recordId.equals(getCurrentUser().getSysUser().getSysUserId())){
            throw new BizException("系统不允许删除当前登陆用户！");
        }
        // 删除用户
        sysUserService.removeUser(sysUser, CS.SYS_TYPE.MGR);

        //如果用户被删除，需要更新redis数据
        authService.refAuthentication(Arrays.asList(recordId));

        return ApiRes.ok();
    }
}
