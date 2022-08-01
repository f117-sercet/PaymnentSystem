package com.pay.pay.service.mapper;

import com.pay.pay.core.entity.SysUserAuth;
import com.pay.pay.core.entity.SysUserRoleRela;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 操作员<->角色 关联表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface SysUserRoleRelaMapper extends BaseMapper<SysUserRoleRela> {

    SysUserAuth selectByLogin(@Param("identifier")String identifier,
                              @Param("identityType")Byte identityType, @Param("sysType")String sysType);
}
