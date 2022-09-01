package com.pay.components.oss.service;

import com.pay.components.oss.constant.OssSavePlaceEnum;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description： 接口
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 17:45
 */
public interface IOssService {
    /** 上传文件 & 生成下载/预览URL **/
    String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName);

    /** 将文件下载到本地
     * 返回是否 写入成功
     * false: 写入失败， 或者文件不存在
     * **/
    boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target);
}
