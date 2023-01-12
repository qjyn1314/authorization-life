package com.authorization.core.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 服务注册到nacos类
 *
 * @author wangjunming
 */
@Slf4j
@AutoConfiguration
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
