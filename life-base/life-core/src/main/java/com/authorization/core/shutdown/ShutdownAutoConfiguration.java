package com.authorization.core.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.authorization.redis.start.util.RedisService;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 服务关闭配置
 */
@AutoConfiguration
public class ShutdownAutoConfiguration {

    @Resource
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;
    @Resource
    private RedisService stringRedisService;

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat(nacosAutoServiceRegistration, stringRedisService);
    }
}
