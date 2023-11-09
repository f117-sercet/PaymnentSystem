package com.payment.pay.mch.mq;

import com.pay.components.mq.model.ResetAppConfigMQ;
import com.pay.payMbg.service.impl.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description： 接收MQ消息
 *
 * 更新系统配置参数
 * @author: 段世超
 * @aate: Created in 2023/3/1 18:29
 */
@Component
@Slf4j
public class ResetAppConfigMQReceiver implements ResetAppConfigMQ.IMQReceiver {

    @Resource
    private SysConfigService sysConfigService;
    @Override
    public void receive(ResetAppConfigMQ.MsgPayload payload) {

        log.info("成功接收更新系统配置的订阅通知，msg={}" + payload);
        sysConfigService.initDBConfig(payload.getGroupKey());
        log.info("系统配置静态属性已重置");

    }
}
