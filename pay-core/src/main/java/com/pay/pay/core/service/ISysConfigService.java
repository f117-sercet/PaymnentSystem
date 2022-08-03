package com.pay.pay.core.service;

import com.pay.pay.core.model.DBApplicationConfig;

/**
 * Description： 获取应用配置参数
 *
 * @author: 段世超
 * @aate: Created in 2022/8/3 17:28
 */
public interface ISysConfigService {

    DBApplicationConfig getDBApplicationConfig();
}
