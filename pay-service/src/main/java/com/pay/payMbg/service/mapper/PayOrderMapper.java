package com.pay.payMbg.service.mapper;

import com.pay.pay.core.entity.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付订单表 Mapper 接口
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

       Map payCount(Map param);

       List<Map>payTypeCount(Map param);

       List<Map> selectOrderCount(Map map);

    /** 更新订单退款金额和次数 **/
    int updateRefundAmountAndCount(@Param("payOrderId") String payOrderId, @Param("currentRefundAmount") Long currentRefundAmount);



}
