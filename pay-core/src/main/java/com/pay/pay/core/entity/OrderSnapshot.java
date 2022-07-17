package com.pay.pay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单接口数据快照
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_snapshot")
public class OrderSnapshot implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单类型: 1-支付, 2-退款
     */
    private Byte orderType;

    /**
     * 下游请求数据
     */
    private String mchReqData;

    /**
     * 下游请求时间
     */
    private Date mchReqTime;

    /**
     * 向下游响应数据
     */
    private String mchRespData;

    /**
     * 向下游响应时间
     */
    private Date mchRespTime;

    /**
     * 向上游请求数据
     */
    private String channelReqData;

    /**
     * 向上游请求时间
     */
    private Date channelReqTime;

    /**
     * 上游响应数据
     */
    private String channelRespData;

    /**
     * 上游响应时间
     */
    private Date channelRespTime;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;


}
