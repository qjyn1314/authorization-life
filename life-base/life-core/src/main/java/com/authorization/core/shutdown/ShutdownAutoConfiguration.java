package com.authorization.core.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.authorization.redis.start.service.StringRedisService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 服务关闭配置
 */
@AutoConfiguration
public class ShutdownAutoConfiguration {

    @Resource
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;
    @Resource
    private StringRedisService stringRedisService;

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat(nacosAutoServiceRegistration, stringRedisService);
    }
}
