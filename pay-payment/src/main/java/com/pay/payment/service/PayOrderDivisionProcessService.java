package com.pay.payment.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pay.components.mq.model.PayOrderDivisionMQ;
import com.pay.pay.core.entity.MchDivisionReceiver;
import com.pay.pay.core.entity.PayOrder;
import com.pay.pay.core.entity.PayOrderDivisionRecord;
import com.pay.pay.core.exeception.BizException;
import com.pay.pay.core.utils.AmountUtil;
import com.pay.pay.core.utils.SeqKit;
import com.pay.pay.service.impl.MchDivisionReceiverGroupService;
import com.pay.pay.service.impl.MchDivisionReceiverService;
import com.pay.pay.service.impl.PayOrderDivisionRecordService;
import com.pay.pay.service.impl.PayOrderService;
import com.pay.payment.rqrs.msg.ChannelRetMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Description： 支付订单分账处理逻辑
 *
 * @author: 段世超
 * @aate: Created in 2023/4/7 15:48
 */
@Slf4j
@Component
public class PayOrderDivisionProcessService {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private MchDivisionReceiverService mchDivisionReceiverService;
    @Resource
    private MchDivisionReceiverGroupService mchDivisionReceiverGroupService;
    @Resource
    private PayOrderDivisionRecordService payOrderDivisionRecordService;
    @Resource
    private ConfigContextQueryService configContextQueryService;

    public ChannelRetMsg processPayOrderDivision(String payOrderId, Byte useSysAutoDivisionReceivers, List<PayOrderDivisionMQ.CustomerDivisionReceiver> receiverList, Boolean isResend) {

        if (isResend = null) {
            isResend = false;
        }
        String logPrefix = "订单[" + payOrderId + "]执行分账";

        // 查询订单信息
        PayOrder payOrder = payOrderService.getById(payOrderId);

        if (payOrder == null) {
            log.error("{},订单不存在", logPrefix);
            throw new BizException("订单不存在");
        }
        // 分账状态不正确
        if (payOrder.getDivisionState() != PayOrder.DIVISION_STATE_WAIT_TASK && payOrder.getDivisionState() != PayOrder.DIVISION_STATE_UNHAPPEN) {
            log.error("{}, 分账状态不正确", logPrefix);
            throw new BizException("分账状态不正确");
        }
        //更新订单为： 分账任务处理中
        boolean updPayOrder = payOrderService.update(new LambdaUpdateWrapper<PayOrder>()
                .set(PayOrder::getDivisionState, PayOrder.DIVISION_STATE_ING)
                .eq(PayOrder::getPayOrderId, payOrderId)
                .eq(PayOrder::getDivisionState, payOrder.getDivisionState()));
        if (!updPayOrder) {
            log.error("{}, 更新支付订单为分账处理中异常！", logPrefix);
            throw new BizException("更新支付订单为分账处理中异常");
        }

        //所有分账列表
        List<PayOrderDivisionRecord> recordList = null;

        // 重发通知
        if (isResend) {
            recordList = payOrderDivisionRecordService.list(PayOrderDivisionRecord.gw().eq(PayOrderDivisionRecord::getPayOrderId, payOrderId));
        } else {

            // 查询&过滤 所有的分账接收对象
            List<MchDivisionReceiver> allReceiver = this.queryReceiver(useSysAutoDivisionReceivers, payOrder, receiverList);

            // 得到全部分账比例(所有待分账账号的分账比例总和)
            BigDecimal allDivisionProfit = BigDecimal.ZERO;
            for (MchDivisionReceiver receiver : allReceiver) {
                allDivisionProfit = allDivisionProfit.add(receiver.getDivisionProfit());
            }
            //计算分账金额 = 商家实际入账金额
            Long payOrderDivisionAmount = payOrderService.calMchIncomeAmount(payOrder);
            //剩余待分账金额 (用作最后一个分账账号的 计算， 避免出现分账金额超出最大) [结果向下取整 ， 避免出现金额溢出的情况。 ]
            Long subDivisionAmount = AmountUtil.calPercentageFee(payOrderDivisionAmount, allDivisionProfit, BigDecimal.ROUND_FLOOR);
            recordList = new ArrayList<>();

            // 计算订单分账金额，并插入到纪录列表
            String batchOrderId = SeqKit.genDivisionBatchId();
            for (MchDivisionReceiver receiver : allReceiver) {
                PayOrderDivisionRecord record = genRecord(batchOrderId, payOrder, receiver, payOrderDivisionAmount, subDivisionAmount);

                //剩余金额
                subDivisionAmount = subDivisionAmount - record.getCalDivisionAmount();

                //入库保存
                payOrderDivisionRecordService.save(record);
                recordList.add(record);
            }

        }
        ChannelRetMsg channelRetMsg = null;

    }

    private PayOrderDivisionRecord genRecord(String batchOrderId, PayOrder payOrder, MchDivisionReceiver receiver, Long payOrderDivisionAmount, Long subDivisionAmount) {
    }

    private List<MchDivisionReceiver> queryReceiver(Byte useSysAutoDivisionReceivers, PayOrder payOrder, List<PayOrderDivisionMQ.CustomerDivisionReceiver> customerDivisionReceiverList){


    }
}
