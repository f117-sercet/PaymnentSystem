package com.pay.components.oss.ctrl;

import com.pay.components.oss.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description： 统一文件上传接口（ossFile）
 *
 * @author: 段世超
 * @aate: Created in 2022/9/1 17:08
 */
@RestController
@RequestMapping("/api/ossFile")
public class OssFileController {

    @Autowired
    private IOssService service;
}
