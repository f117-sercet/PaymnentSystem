package com.pay.pay.core.entity;

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
 * 商户通知记录表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_mch_notify_record")
public class MchNotifyRecord implements Serializable {

    //订单类型:1-支付,2-退款, 3-转账
    public static final byte TYPE_PAY_ORDER = 1;
    public static final byte TYPE_REFUND_ORDER = 2;
    public static final byte TYPE_TRANSFER_ORDER = 3;

    //通知状态
    public static final byte STATE_ING = 1;
    public static final byte STATE_SUCCESS = 2;
    public static final byte STATE_FAIL = 3;

    public static final LambdaQueryWrapper<MchNotifyRecord> gw(){
        return new LambdaQueryWrapper<>();
    }
    private static final long serialVersionUID=1L;

    /**
     * 商户通知记录ID
     */
    @TableId(value = "notify_id", type = IdType.AUTO)
    private Long notifyId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单类型:1-支付,2-退款
     */
    private Byte orderType;

    /**
     * 商户订单号
     */
    private String mchOrderNo;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 服务商号
     */
    private String isvNo;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 通知响应结果
     */
    private String resResult;

    /**
     * 通知次数
     */
    private Integer notifyCount;

    /**
     * 最大通知次数, 默认6次
     */
    private Integer notifyCountLimit;

    /**
     * 通知状态,1-通知中,2-通知成功,3-通知失败
     */
    private Byte state;

    /**
     * 最后一次通知时间
     */
    private Date lastNotifyTime;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
