package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户认证表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_user_auth")
public class SysUserAuth implements Serializable {

    public static final LambdaQueryWrapper<SysUserAuth> gw(){
        return new LambdaQueryWrapper<>();
    }
    private static final long serialVersionUID=1L;

    /**
     * ID
     */
    @TableId(value = "auth_id", type = IdType.AUTO)
    private Long authId;

    /**
     * user_id
     */
    private Long userId;

    /**
     * 登录类型  1-登录账号 2-手机号 3-邮箱  10-微信  11-QQ 12-支付宝 13-微博
     */
    private Byte identityType;

    /**
     * 认证标识 ( 用户名 | open_id )
     */
    private String identifier;

    /**
     * 密码凭证
     */
    private String credential;

    /**
     * salt
     */
    private String salt;

    /**
     * 所属系统： MGR-运营平台, MCH-商户中心
     */
    private String sysType;


}
