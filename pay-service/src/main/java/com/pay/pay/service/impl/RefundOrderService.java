package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.RefundOrder;
import com.pay.pay.service.mapper.PayOrderMapper;
import com.pay.pay.service.mapper.RefundOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 退款订单表 服务实现类
 *
 * @author: 段世超
 * @aate: Created in 2022/8/22 18:17
 */
@Service
public class RefundOrderService extends ServiceImpl<RefundOrderMapper, RefundOrder> {

    @Resource
    private PayOrderMapper payOrderMapper;
}
