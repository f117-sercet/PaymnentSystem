package com.pay.pay.service.mapper;

import com.pay.pay.core.entity.SysRoleEntRela;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色权限关联表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface SysRoleEntRelaMapper extends BaseMapper<SysRoleEntRela> {

    List<String> selectEntIdsByUserId(@Param("userId") Long userId, @Param("sysType") String sysType);
}
