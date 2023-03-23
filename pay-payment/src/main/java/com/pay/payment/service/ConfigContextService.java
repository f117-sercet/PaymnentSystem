package com.pay.payment.service;

import com.pay.payment.model.IsvConfigContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置上下文信息
 *
 * @author: 段世超
 * @aate: Created in 2023/3/22 10:06
 */
@Slf4j
@Service
public class ConfigContextService {

    /** <商户ID, 商户配置项>  **/
    private static final Map<String, MchInfoConfigContext> mchInfoConfigContextMap = new ConcurrentHashMap<>();

    /** <应用ID, 商户配置上下文>  **/
    private static final Map<String, MchAppConfigContext> mchAppConfigContextMap = new ConcurrentHashMap<>();

    /** <服务商号, 服务商配置上下文>  **/
    private static final Map<String, IsvConfigContext> isvConfigContextMap = new ConcurrentHashMap<>();

    public Object getMchAppConfigContext(String mchNo, String mchAppId) {
    }
}
