package com.pay.mgr.ctrl.config;

import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.model.ApiRes;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.service.impl.SysEntitlementService;
import com.pay.pay.service.impl.SysUserAuthService;
import com.pay.pay.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 当前登录者的信息相关接口
 *
 * @author: 段世超
 * @aate: Created in 2022/10/8 18:25
 */
@RestController
@RequestMapping("api/current")
public class CurrentUserController extends CommonCtrl {

    @Autowired
    private SysEntitlementService sysEntitlementService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserAuthService sysUserAuthService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ApiRes currentUserInfo(){
        ///当前用户信息
        JeeUserDetails jeeUserDetails = getCurrentUser();
        SysUser user = jeeUserDetails.getSysUser();
        return null;
    }


}
