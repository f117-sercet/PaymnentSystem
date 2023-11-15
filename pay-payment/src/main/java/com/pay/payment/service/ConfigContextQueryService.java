package com.pay.payment.service;

import com.pay.payMbg.service.impl.MchAppService;
import com.pay.payMbg.service.impl.MchInfoService;
import com.pay.payMbg.service.impl.PayInterfaceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description： 配置信息查询服务 （兼容 缓存 和 直接查询方式）
 *
 * @author: 段世超
 * @aate: Created in 2023/11/15 17:54
 */
@Slf4j
@Service
public class ConfigContextQueryService {

    @Autowired
    ConfigContextService configContextService;
    @Autowired private MchInfoService mchInfoService;
    @Autowired private MchAppService mchAppService;
    @Autowired private PayInterfaceConfigService payInterfaceConfigService;
}
