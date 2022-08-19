package com.pay.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description： 支付订单表，服务实现类
 *
 * @author: 段世超
 * @aate: Created in 2022/8/8 9:21
 */
@Service
public class PayOrderService extends ServiceImpl<PayOrderMapper, PayOrder> {


    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private MchInfoMapper mchInfoMapper;
    @Resource
    private IsvInfoMapper isvInfoMapper;
    @Resource
    private PayWayMapper payWayMapper;
    @Resource
    private PayOrderDivisionRecordMapper payOrderDivisionRecordMapper;

    /** 更新订单状态  【订单生成】 --》 【支付中】 **/
    public boolean updateInit2Ing(String payOrderId,PayOrder payOrder){

        PayOrder updateRecord = new PayOrder();
        updateRecord.setState(PayOrder.STATE_ING);

        //同时更新， 未确定 --》 已确定的其他信息。  如支付接口的确认、 费率的计算。
        updateRecord.setIfCode(payOrder.getIfCode());
        updateRecord.setWayCode(payOrder.getWayCode());
        updateRecord.setMchFeeAmount(payOrder.getMchFeeAmount());
        updateRecord.setChannelUser(payOrder.getChannelUser());

        return update(updateRecord, new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getPayOrderId, payOrderId).eq(PayOrder::getState, PayOrder.STATE_INIT));

    }
}
