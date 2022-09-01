package com.pay.components.oss.constant;

import lombok.Getter;

/**
 * Description： oss 服务枚举值
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 17:06
 */
@Getter
public enum OssServiceTypeEnum {

    LOCAL("local"), //本地存储

    ALIYUN_OSS("aliyun-oss");  //阿里云oss

    /** 名称 **/
    private String serviceName;

    OssServiceTypeEnum(String serviceName){
        this.serviceName = serviceName;
    }

}
