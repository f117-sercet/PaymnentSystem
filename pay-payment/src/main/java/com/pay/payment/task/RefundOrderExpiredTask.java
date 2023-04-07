package com.pay.payment.task;

import com.pay.pay.service.impl.RefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Description： 退款订单过期定时任务
 *
 * @author: 段世超
 * @aate: Created in 2023/4/7 15:45
 */
@Slf4j
@Component
public class RefundOrderExpiredTask {
    @Autowired
    private RefundOrderService refundOrderService;

    @Scheduled(cron="0 0/1 * * * ?") // 每分钟执行一次
    public void start() {

        int updateCount = refundOrderService.updateOrderExpired();
        log.info("处理退款订单超时{}条.", updateCount);
    }
}
