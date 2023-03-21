package com.pay.payment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description： 系统Yml配置参数定义Bean
 *
 * @author: 段世超
 * @aate: Created in 2023/3/13 10:20
 */
@Component
@ConfigurationProperties(prefix="isys")
@Data
public class SystemYmlConfig {

    /** 是否允许跨域请求 [生产环境建议关闭，
     * 若api与前端项目没有在同一个域名下时，
     * 应开启此配置或在nginx统一配置允许跨域]  **/
    private Boolean allowCors;

    /** 是否内存缓存配置信息:
     * true表示开启如支付网关地址/商户应用配置/服务商配置等，
     * 开启后需检查MQ的广播模式是否正常；
     * false表示直接查询DB.  **/
    private Boolean cacheConfig;
}