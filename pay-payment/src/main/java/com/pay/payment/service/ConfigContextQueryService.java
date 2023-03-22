package com.pay.payment.service;

import com.pay.pay.service.impl.MchAppService;
import com.pay.pay.service.impl.MchInfoService;
import com.pay.pay.service.impl.PayInterfaceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 配置信息查询服务 （兼容 缓存 和 直接查询方式）
 *
 * @author: 段世超
 * @aate: Created in 2023/3/22 9:51
 */
@Slf4j
@Service
public class ConfigContextQueryService {

    @Resource
    ConfigContextService configContextService;
    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private MchAppService mchAppService;
    @Resource
    private PayInterfaceConfigService payInterfaceConfigService;
}
