package com.pay.components.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description： 系统Yml配置参数定义Bean
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 16:48
 */
@Data
@Component
@ConfigurationProperties(prefix="isys")
public class OssYmlConfig {

    private  Oss oss;

    @Data
    public static class Oss{

        /** 存储根路径 **/
        private String fileRootPath;

        /** 公共读取块 **/
        private String filePublicPath;

        /** 私有读取块 **/
        private String filePrivatePath;

        /** oss类型 **/
        private String serviceType;

    }
}
