package com.pay.payMbg.service.mapper;

import com.pay.pay.core.entity.RefundOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 退款订单表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface RefundOrderMapper extends BaseMapper<RefundOrder> {

       /****查询全部退成功金额***/
      Long sumSuccessRefundAmount(String payOrderId);

}
