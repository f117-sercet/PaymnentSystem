package com.pay.payment.service;

import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.utils.SpringBeansUtil;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.payment.channel.IPayOrderQueryService;
import com.pay.payment.rqrs.msg.ChannelRetMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 查询上游订单， &  补单服务实现类
 *
 * @author: 段世超
 * @aate: Created in 2023/3/22 9:50
 */
@Slf4j
@Service
public class ChannelOrderReissueService {

    @Resource
    private ConfigContextQueryService configContextQueryService;
    @Resource
    private PayOrderService payOrderService;
    @Resource
    private PayOrderProcessService payOrderProcessService;
    @Resource
    private RefundOrderProcessService refundOrderProcessService;


    public ChannelRetMsg processPayOrder(PayOrder payOrder){


        try {
            String payOrderId = payOrder.getPayOrderId();

            // 查询支付接口是否存在
            IPayOrderQueryService queryService = SpringBeansUtil.getBean(payOrder.getIfCode() + "PayOrderQueryService", IPayOrderQueryService.class);

            // 支付通道接口实现不存在
            if (queryService==null) {
                log.error("{} interface not exists",payOrder.getIfCode());
                return null;
            }
            // 查询出商户应用的配置信息

            configContextQueryService.queryMchInfoAndAppInfo(payOrder.getMchNo(), payOrder.getAppId());

        }


    }
}
