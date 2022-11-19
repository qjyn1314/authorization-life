package com.authorization.core.server;

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
                                                      RedisTemplate stringRedisTemplate) {
        log.debug("CustomServiceRegistry Init ......NacosDiscoveryProperties-{}", nacosDiscoveryProperties);
        log.debug("CustomServiceRegistry Init ......NacosServiceManager-{}", nacosServiceManager);
        log.debug("CustomServiceRegistry Init ......RedisTemplate-{}", stringRedisTemplate);
        return new CustomServiceRegistry(nacosDiscoveryProperties, nacosServiceManager, stringRedisTemplate);
    }

}
