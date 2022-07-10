package com.authserver.common.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务关闭配置
 */
@Configuration
public class ShutdownAutoConfiguration {

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat(NacosAutoServiceRegistration nacosRegistration,
                                                         RedisTemplate redisTemplate) {
        return new GracefulShutdownTomcat(nacosRegistration, redisTemplate);
    }
}
