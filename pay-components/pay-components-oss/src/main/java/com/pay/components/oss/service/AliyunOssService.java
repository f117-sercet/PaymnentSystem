package com.pay.components.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.pay.components.oss.config.AliyunOssYmlConfig;
import com.pay.components.oss.config.OssYmlConfig;
import com.pay.components.oss.constant.OssSavePlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Description： 阿里云OSS 实现类
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 17:46
 */
@Service
@Slf4j
@ConditionalOnProperty(name="isys.oss.service-type",havingValue = "aliyun-oss")
public class AliyunOssService implements IOssService {

    @Resource
    private AliyunOssYmlConfig aliyunOssYmlConfig;
    @Resource
    private OssYmlConfig osSymlconfig;
    private OSS ossClient = null;

    @PostConstruct
    public void init(){
       ossClient = new OSSClientBuilder()
               .build(aliyunOssYmlConfig
                       .getEndpoint()
               ,aliyunOssYmlConfig.getAccessKeyId(),
                       aliyunOssYmlConfig.getAccessKeySecret());

    }

    /***
     * 处理文件保存路径
     * @param ossSavePlaceEnum 保存位置
     * @param multipartFile  文件路径
     * @param saveDirAndFileName
     * @return 完整路径。
     */
    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        return null;
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}
