package com.pay.components.mq.model;

import com.pay.components.mq.constant.MQSendTypeEnum;

/**
 * Description： 定义MQ消息格式
 *
 * @author: 段世超
 * @aate: Created in 2022/8/5 10:53
 */
public abstract class AbstractMQ {


    /** MQ名称 **/
    public abstract String getMQName();

    /** MQ 类型 **/
    public abstract MQSendTypeEnum getMQType();

    /** 构造MQ消息体 String类型 **/
    public abstract String toMessage();
}
