package com.pay.mgr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Description： Redis配置类
 *
 * @author: 段世超
 * @aate: Created in 2022/9/13 18:12
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${spring.redis.database}")
    private Integer defaultDatabase;

    @Value("${spring.redis.password}")
    private String password;


    /**************当前系统的redis缓存操作对象******************/

    @Primary
    @Bean(name = "defaultStringRedisTemplate")
    public StringRedisTemplate sysStringRedisTemplate() {

        StringRedisTemplate template = new StringRedisTemplate();

        LettuceConnectionFactory jedisConnectionFactory = new LettuceConnectionFactory();

        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        return template;

    }
}
