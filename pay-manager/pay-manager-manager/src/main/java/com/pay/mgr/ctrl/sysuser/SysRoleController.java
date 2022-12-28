package com.pay.mgr.ctrl.sysuser;

import com.pay.mgr.ctrl.common.CommonCtrl;
import com.pay.pay.service.impl.SysRoleEntRelaService;
import com.pay.pay.service.impl.SysRoleService;
import com.pay.pay.service.impl.SysUserRoleRelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 角色管理
 *
 * @author: 段世超
 * @aate: Created in 2022/12/28 10:12
 */
@RestController
@RequestMapping("api/sysRoles")
public class SysRoleController extends CommonCtrl {
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysUserRoleRelaService sysUserRoleRelaService;
    @Autowired private AuthService authService;
    @Autowired private SysRoleEntRelaService sysRoleEntRelaService;

}
