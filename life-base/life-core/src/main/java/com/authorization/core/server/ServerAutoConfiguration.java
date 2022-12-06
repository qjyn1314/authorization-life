package com.authorization.core.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
public class ServerAutoConfiguration {

    @Bean
    @Primary
    public NacosServiceRegistry customServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,
                                                      NacosServiceManager nacosServiceManager,
                                                      StringRedisService stringRedisService) {
        log.debug("CustomServiceRegistry Init ......NacosDiscoveryProperties-{}", nacosDiscoveryProperties);
        log.debug("CustomServiceRegistry Init ......RedisTemplate-{}", stringRedisService);
        return new CustomServiceRegistry(nacosDiscoveryProperties, stringRedisService);
    }

}
