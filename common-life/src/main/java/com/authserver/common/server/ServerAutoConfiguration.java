package com.authserver.common.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@Configuration
public class ServerAutoConfiguration {

    @Bean
    @Primary
    public NacosServiceRegistry customServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,
                                                      NacosServiceManager nacosServiceManager,
                                                      RedisTemplate redisHelper) {
        log.info("CustomServiceRegistry Init ......NacosDiscoveryProperties-{}", nacosDiscoveryProperties);
        log.info("CustomServiceRegistry Init ......NacosServiceManager-{}", nacosServiceManager);
        log.info("CustomServiceRegistry Init ......RedisTemplate-{}", redisHelper);
        return new CustomServiceRegistry(nacosDiscoveryProperties, nacosServiceManager, redisHelper);
    }

}
