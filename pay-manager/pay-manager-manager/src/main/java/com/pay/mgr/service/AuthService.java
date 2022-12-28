package com.pay.mgr.service;

import com.pay.mgr.config.SysTemYmlConfig;
import com.pay.pay.service.impl.SysRoleEntRelaService;
import com.pay.pay.service.impl.SysRoleService;
import com.pay.pay.service.impl.SysUserService;
import com.pay.pay.service.mapper.SysEntitlementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description： 认证配置
 *
 * @author: 段世超
 * @aate: Created in 2022/12/28 10:15
 */
@Slf4j
@Service
public class AuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysUserService sysUserService;
    @Autowired private SysRoleService sysRoleService;
    @Autowired private SysRoleEntRelaService sysRoleEntRelaService;
    @Autowired private SysEntitlementMapper sysEntitlementMapper;
    @Autowired private SysTemYmlConfig systemYmlConfig;

    public String auth(String username, String password){

        //1. 生成spring-security usernamePassword类型对象
        new UsernamePasswordAuthenticationToken(username, password);

    }


}
