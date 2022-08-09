package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.SysUser;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.service.mapper.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2022/8/8 10:46
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Autowired
    private SysUserAuthService sysUserAuthService;
    @Autowired
    private SysUserRoleRelaService sysUserRoleRelaService;

    /** 添加系统用户 **/
    @Transactional
    public void addSysUser(SysUser sysUser, String sysType){

        // 判断数据来源
        if (StringUtils.isEmpty(sysUser.getLoginUsername())){

            throw  new BizException("登录用户不能为空");
        }

    }
}
