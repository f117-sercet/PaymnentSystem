package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统角色权限关联表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_role_ent_rela")
public class SysRoleEntRela implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 权限ID
     */
    private String entId;


}
