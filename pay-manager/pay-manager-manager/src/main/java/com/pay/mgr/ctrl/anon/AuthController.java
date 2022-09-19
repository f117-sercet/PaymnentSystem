package com.pay.mgr.ctrl.anon;

import com.pay.mgr.config.SysTemYmlConfig;
import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.model.security.JeeUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;

/**
 * Description： 认证接口
 *
 * @author: 段世超
 * @aate: Created in 2022/9/19 18:35
 */
public abstract class AuthController extends CommonCtrl {

    @Resource
    protected SysTemYmlConfig mainConfig;


    /** 获取当前用户ID */
    protected JeeUserDetails getCurrentUser(){

        return (JeeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
