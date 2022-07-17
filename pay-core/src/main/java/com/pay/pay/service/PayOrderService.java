package com.pay.pay.service;

import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.service.mapper.PayOrderMapper;
import com.pay.pay.delete_delete.IPayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付订单表 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-07-17
 */
@Service
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

}
