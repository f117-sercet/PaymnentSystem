package com.pay.payment.service;

import com.jeequan.jeepay.util.StringUtils;
import com.pay.components.mq.vender.IMQSender;
import com.pay.pay.core.entity.MchNotifyRecord;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.service.impl.MchNotifyRecordService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Description：
 * 商户通知 service
 *
 * @author: 段世超
 * @create: Created in 2023/3/30 14:50
 */
@Slf4j
public class PayMchNotifyService {

    @Resource
    private MchNotifyRecordService mchNotifyRecordService;
    @Resource
    private ConfigContextQueryService configContextQueryService;
    @Resource
    private IMQSender mqSender;


    public void payOrderNotify(PayOrder dbPayOrder){

        // 商户通知信息，只有订单是终态，才会发通知，如明确成功和明确失败。
        if (StringUtils.isEmpty(dbPayOrder.getNotifyUrl())) {
            return;
        }
        // 获取通知对象
        MchNotifyRecord mchNotifyRecord = mchNotifyRecordService.findByPayOrder(dbPayOrder.getPayOrderId());

        if (mchNotifyRecord!=null) {
            log.info("当前已存在通知信息，不在发送");
            return;
        }

        //商户app私钥
        String appSecret = configContextQueryService.queryMchApp(dbPayOrder.getMchNo(), dbPayOrder.getAppId()).getAppSecret();

    }
}
