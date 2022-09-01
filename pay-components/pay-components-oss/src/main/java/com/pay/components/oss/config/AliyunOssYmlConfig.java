package com.pay.components.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description： aliyun oss 参数配置
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 16:42
 */
@Data
@Component
@ConfigurationProperties(prefix="isys.oss.aliyun-oss")
public class AliyunOssYmlConfig {

    private String endpoint;
    private String publicBucketName;
    private String privateBucketName;
    private String accessKeyId;
    private String accessKeySecret;
}
