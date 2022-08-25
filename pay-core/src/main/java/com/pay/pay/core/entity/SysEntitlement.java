package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pay.pay.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2021-04-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_entitlement")
public class SysEntitlement extends BaseModel {

    //gw
    public static final LambdaQueryWrapper<SysEntitlement> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 权限ID[ENT_功能模块_子模块_操作], eg: ENT_ROLE_LIST_ADD
     */
    @TableId
    private String entId;

    /**
     * 权限名称
     */
    private String entName;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单uri/路由地址
     */
    private String menuUri;

    /**
     * 组件Name（前后端分离使用）
     */
    private String componentName;

    /**
     * 权限类型 ML-左侧显示菜单, MO-其他菜单, PB-页面/按钮
     */
    private String entType;

    /**
     * 快速开始菜单 0-否, 1-是
     */
    private Byte quickJump;

    /**
     * 状态 0-停用, 1-启用
     */
    private Byte state;

    /**
     * 父ID
     */
    private String pid;

    /**
     * 排序字段, 规则：正序
     */
    private Integer entSort;

    /**
     * 所属系统： MGR-运营平台, MCH-商户中心
     */
    private String sysType;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
