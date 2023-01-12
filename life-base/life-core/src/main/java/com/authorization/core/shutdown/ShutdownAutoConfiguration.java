package com.authorization.core.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.authorization.redis.start.service.StringRedisService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 服务关闭配置
 */
@AutoConfiguration
public class ShutdownAutoConfiguration {

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat(NacosAutoServiceRegistration nacosRegistration,
                                                         StringRedisService stringRedisService) {
        return new GracefulShutdownTomcat(nacosRegistration, stringRedisService);
    }
}
