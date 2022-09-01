package com.pay.components.oss.service;

import com.pay.components.oss.constant.OssSavePlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        return null;
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}
