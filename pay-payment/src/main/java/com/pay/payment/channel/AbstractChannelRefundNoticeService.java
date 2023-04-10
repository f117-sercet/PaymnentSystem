package com.pay.payment.channel;

import com.pay.pay.core.beans.RequestKitBean;
import com.pay.payment.service.ConfigContextQueryService;
import com.pay.payment.util.ChannelCertConfigKitBean;

import javax.annotation.Resource;

/**
 * 实现退款回调接口抽象类
 *
 * @author: 段世超
 * @aate: Created in 2023/3/22 9:48
 */
public abstract class AbstractChannelRefundNoticeService implements IChannelRefundNoticeService {

    @Resource
    private RequestKitBean requestKitBean;
    @Resource
    private ChannelCertConfigKitBean channelCertConfigKitBean;
    @Resource
    protected ConfigContextQueryService configContextQueryService;

}
