package com.pay.payment.bootstrap;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.pay.payMbg.service.impl.SysConfigService;
import com.pay.payment.config.SystemYmlConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 比如初始化配置文件， 读取基础数据， 资源初始化等。 避免在Main函数中写业务代码。
 *  CommandLineRunner  /
 *  ApplicationRunner都可以达到要求， 只是调用参数有所不同
 *
 * @author: 段世超
 * @aate: Created in 2023/3/13 10:07
 */

@Component
public class initRunner implements CommandLineRunner {

    @Resource
    private SystemYmlConfig systemYmlConfig;
    @Override
    public void run(String... args) throws Exception {

        // 配置是否使用缓存模式
        SysConfigService.IS_USE_CACHE = systemYmlConfig.getCacheConfig();
        //初始化处理fastjson格式
        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(DatePattern.NORM_DATETIME_PATTERN));

        //解决json 序列化时候的  $ref：问题
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();

    }
}
