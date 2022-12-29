package com.pay.mgr.service;

import com.pay.mgr.config.SysTemYmlConfig;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.exeception.JeepayAuthenticationException;
import com.pay.pay.core.model.security.JeeUserDetails;
import com.pay.pay.service.impl.SysRoleEntRelaService;
import com.pay.pay.service.impl.SysRoleService;
import com.pay.pay.service.impl.SysUserService;
import com.pay.pay.service.mapper.SysEntitlementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);

        //spring-security 自动认证过程；
        // 1. 进入 JeeUserDetailsServiceImpl.loadUserByUsername 获取用户基本信息；
        //2. SS根据UserDetails接口验证是否用户可用；
        //3. 最后返回loadUserByUsername 封装的对象信息

        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(upToken);
        } catch (JeepayAuthenticationException jex) {
            throw jex.getBizException() == null ? new BizException(jex.getMessage()) : jex.getBizException();
        } catch (BadCredentialsException e) {
            throw new BizException("用户名/密码错误！");
        } catch (AuthenticationException e) {
            log.error("AuthenticationException:", e);
            throw new BizException("认证服务出现异常， 请重试或联系系统管理员！");
        }

        JeeUserDetails principal = (JeeUserDetails) authentication.getPrincipal();

        //返回JWTToken
        return  "";

    }


}
