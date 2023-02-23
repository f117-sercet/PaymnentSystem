package com.payment.pay.mch.ctrl;

import com.pay.pay.core.constants.ApiCodeEnum;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.ctrls.AbstractCtrl;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.service.impl.SysConfigService;
import com.payment.pay.mch.config.SystemYmlConfig;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;

/**
 * 抽象公共Ctrl
 *
 * @author: 段世超
 * @aate: Created in 2023/1/12 18:14
 */
public abstract class CommonCtrl  extends AbstractCtrl {

    @Resource
    protected SystemYmlConfig mainConfig;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 获取当前用户ID
     */
    protected JeeUserDetails getCurrentUser() {

        return (JeeUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /****获取当前用户Id******/
    protected String getCurrentMchNo() {
        return getCurrentUser().getSysUser().getBelongInfoId();
    }

    /**
     * 获取当前用户登录IP
     *
     * @return
     */
    protected String getIp() {
        return getClientIp();
    }


    protected ApiRes checkIsAdmin() {
        SysUser sysUser = getCurrentUser().getSysUser();

        if (sysUser.getIsAdmin() != CS.YES) {

            return ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR);
        } else {
            return null;
        }
    }
}
