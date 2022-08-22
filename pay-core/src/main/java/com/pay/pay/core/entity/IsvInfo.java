package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务商信息表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_isv_info")
public class IsvInfo implements Serializable {

    public static final LambdaQueryWrapper<IsvInfo> gw(){
        return new LambdaQueryWrapper<>();
    }
    private static final long serialVersionUID=1L;

    /**
     * 服务商号
     */
    private String isvNo;

    /**
     * 服务商名称
     */
    private String isvName;

    /**
     * 服务商简称
     */
    private String isvShortName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactTel;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 状态: 0-停用, 1-正常
     */
    private Byte state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者用户ID
     */
    private Long createdUid;

    /**
     * 创建者姓名
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
