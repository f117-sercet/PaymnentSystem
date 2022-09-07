package com.pay.components.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.pay.components.oss.config.AliyunOssYmlConfig;
import com.pay.components.oss.config.OssYmlConfig;
import com.pay.components.oss.constant.OssSavePlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;

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
    public void init() {
        ossClient = new OSSClientBuilder()
                .build(aliyunOssYmlConfig
                                .getEndpoint()
                        , aliyunOssYmlConfig.getAccessKeyId(),
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
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile
            multipartFile, String saveDirAndFileName) {

        String fullPath = getFileKey(ossSavePlaceEnum, saveDirAndFileName);

        try {
            this.ossClient.putObject(
                    ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ? aliyunOssYmlConfig.getPublicBucketName() : aliyunOssYmlConfig.getPrivateBucketName()
                    , fullPath, multipartFile.getInputStream());
            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                return "https://" + aliyunOssYmlConfig.getPublicBucketName() + "." + aliyunOssYmlConfig.getEndpoint() + "/" + fullPath;
            }
            return saveDirAndFileName;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }

    }

    /**
     * 下载文件
     *
     * @param ossSavePlaceEnum
     * @param source
     * @param target
     * @return
     */
    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {

        try {
            String fullPath = getFileKey(ossSavePlaceEnum, source);

            File downloadFile = new File(target);

            // 当本地路径的上层目录不存在时，自动创建
            // OSS SDK 在 Docker 内部可能出现 UnknownHost 错误具体表现为
            // com.aliyun.oss.ClientException: Cannot read the content input stream.
            if (!downloadFile.getParentFile().exists()) {
                log.info("downloadFile parent dir not exists create it: {}",
                        downloadFile.getParentFile().getAbsolutePath());
                downloadFile.getParentFile().mkdirs();
            }

            String bucket = ossSavePlaceEnum == OssSavePlaceEnum.PRIVATE ? aliyunOssYmlConfig.getPrivateBucketName() : aliyunOssYmlConfig.getPublicBucketName();
            this.ossClient.getObject(new GetObjectRequest(bucket, fullPath), downloadFile);

            return true;
        } catch (Exception e) {
            log.error("error", e);
            return false;
        }
    }

    private String getFileKey(OssSavePlaceEnum ossSavePlaceEnum, String filePath) {
// 上传的时候需要考虑 OSS 存储空间的访问权限，并拼接路径前缀
        String filePrefix = ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ? osSymlconfig.getOss()
                .getFilePublicPath() : osSymlconfig.getOss().getFilePrivatePath();

        // 如果路径包含设置的路径前缀，则跳过
        if (filePath.startsWith(filePrefix)) {
            // OSS 不允许路径第一个字符为 /
            if (filePath.indexOf("/") == 0) {
                filePath = filePath.replaceFirst("/", "");
            }
            return filePath;
        }

        String fullPath = (filePrefix + "/" + filePath);

        // OSS 不允许路径第一个字符为 /
        if (fullPath.indexOf("/") == 0) {
            fullPath = fullPath.replaceFirst("/", "");
        }

        return fullPath;
    }
}
