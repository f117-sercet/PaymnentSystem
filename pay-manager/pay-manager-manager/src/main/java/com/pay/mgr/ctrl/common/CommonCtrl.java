package com.pay.mgr.ctrl.common;

import com.pay.mgr.config.SysTemYmlConfig;
import com.pay.pay.core.ctrls.AbstractCtrl;
import com.pay.pay.core.model.security.JeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2022/9/19 18:37
 */
public class CommonCtrl extends AbstractCtrl {

    @Autowired
    protected SysTemYmlConfig mainConfig;

    /** 获取当前用户ID **/
    protected JeeUserDetails getCurrentUser(){

        return (JeeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前用户登录IP
     * @return
     */
    protected String getIp() {
        return getClientIp();
    }

}
