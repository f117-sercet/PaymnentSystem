package com.pay.mgr.mq;

import com.pay.components.mq.model.ResetAppConfigMQ;
import com.pay.payMbg.service.impl.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description： 接受MQ消息
 * 业务：更新系统参数配置
 *
 * @author: 段世超
 * @aate: Created in 2023/1/9 14:29
 */
@Slf4j
@Component
public class ResetAppConfigMQReceiver implements ResetAppConfigMQ.IMQReceiver {
    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public void receive(ResetAppConfigMQ.MsgPayload payload) {

        log.info("成功接收更新系统配置的订阅通知, msg={}", payload);
        sysConfigService.initDBConfig(payload.getGroupKey());
        log.info("系统配置静态属性已重置");
    }
}
