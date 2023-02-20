package com.payment.pay.mch.service;

import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.SysRoleEntRelaService;
import com.pay.pay.service.impl.SysRoleService;
import com.pay.pay.service.impl.SysUserService;
import com.pay.pay.service.mapper.SysEntitlementMapper;
import com.payment.pay.mch.config.SystemYmlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证Service
 *
 * @author: 段世超
 * @aate: Created in 2023/2/20 18:54
 */
@Service
@Slf4j
public class AuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleEntRelaService sysRoleEntRelaService;
    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private SysEntitlementMapper sysEntitlementMapper;
    @Resource
    private SystemYmlConfig systemYmlConfig;

    /**
     * 认证
     * @param username
     * @param password
     * @return
     */
    public String auth(String username, String password){}
}
