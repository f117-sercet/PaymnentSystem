package com.pay.pay.core.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付接口配置参数表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pay_interface_config")
public class PayInterfaceConfig implements Serializable {

    public static final LambdaQueryWrapper<PayInterfaceConfig> gw(){
        return new LambdaQueryWrapper<>();
    }
    private static final long serialVersionUID=1L;


    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号类型:1-服务商 2-商户 3-商户应用
     */
    private Byte infoType;

    /**
     * 服务商号/商户号/应用ID
     */
    private String infoId;

    /**
     * 支付接口代码
     */
    private String ifCode;

    /**
     * 接口配置参数,json字符串
     */
    private String ifParams;

    /**
     * 支付接口费率
     */
    private BigDecimal ifRate;

    /**
     * 状态: 0-停用, 1-启用
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
     * 更新者用户ID
     */
    private Long updatedUid;

    /**
     * 更新者姓名
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
