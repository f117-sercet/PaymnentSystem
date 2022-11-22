package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pay.pay.core.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 支付方式表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pay_way")
public class PayWay extends BaseModel implements Serializable {

    public static final LambdaQueryWrapper<PayWay> gw(){
        return new LambdaQueryWrapper<>();
    }
    private static final long serialVersionUID=1L;

    /**
     * 支付方式代码  例如： wxpay_jsapi
     */
    private String wayCode;

    /**
     * 支付方式名称
     */
    private String wayName;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
