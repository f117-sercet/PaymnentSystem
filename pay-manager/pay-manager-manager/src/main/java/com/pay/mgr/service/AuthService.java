package com.pay.mgr.service;

import cn.hutool.core.util.IdUtil;
import com.pay.mgr.config.SysTemYmlConfig;
import com.pay.pay.core.cache.ITokenService;
import com.pay.pay.core.constants.CS;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.exeception.JeepayAuthenticationException;
import com.pay.pay.core.jwt.JWTPayload;
import com.pay.pay.core.jwt.JWTUtils;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

        JeeUserDetails jeeUserDetails = (JeeUserDetails) authentication.getPrincipal();

        // 验证通过后，在查询用户角色和权限信息集合

        SysUser sysUser = jeeUserDetails.getSysUser();

        //非超级管理员 && 不包含在左侧菜单 进行错误提示
        if(sysUser.getIsAdmin() != CS.YES && sysEntitlementMapper.userHasLeftMenu(sysUser.getSysUserId(), CS.SYS_TYPE.MGR) <= 0){
            throw new BizException("当前用户未分配任何菜单权限，请联系管理员进行分配后再登录！");
        }

        // 放置权限集合
        jeeUserDetails.setAuthorities(getUserAuthority(sysUser));

        //生成token
        String cacheKey = CS.getCacheKeyToken(sysUser.getSysUserId(), IdUtil.fastUUID());

        //生成iToken,并放置到缓存
        ITokenService.processTokenCache(jeeUserDetails,cacheKey); //处理token 缓存信息

       //将信息放置到Spring-security context 中

        UsernamePasswordAuthenticationToken authenticationRest = new UsernamePasswordAuthenticationToken(jeeUserDetails, null, jeeUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //返回JWTToken
        return  JWTUtils.generateToken(new JWTPayload(jeeUserDetails),systemYmlConfig.getJwtSecret());

    }

    private Collection<SimpleGrantedAuthority> getUserAuthority(SysUser sysUser) {

        //用户拥有的角色集合  需要以ROLE_ 开头,  用户拥有的权限集合
        List<String> roleList = sysRoleService.findListByUser(sysUser.getSysUserId());
        List<String> entList = sysRoleEntRelaService.selectEntIdsByUserId(sysUser.getSysUserId(), sysUser.getIsAdmin(), sysUser.getSysType());

        List<SimpleGrantedAuthority> grantedAuthorities = new LinkedList<>();
        roleList.stream().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));
        entList.stream().forEach(ent -> grantedAuthorities.add(new SimpleGrantedAuthority(ent)));
        return grantedAuthorities;

    }


}
