package com.pay.payment.util;

import com.pay.components.oss.config.OssYmlConfig;
import com.pay.components.oss.service.IOssService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 支付平台，获取系统文件工具类
 *
 * @author: 段世超
 * @aate: Created in 2023/3/16 11:51
 */
@Component
public class ChannelCertConfigKitBean {

    @Resource
    private OssYmlConfig ossYmlConfig;
    @Resource
    private IOssService ossService;


}
