package com.authorization.core.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.authorization.start.util.contsant.ServerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
public class CustomServiceRegistry extends NacosServiceRegistry {

    private final RedisTemplate redisTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    public CustomServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,
                                 NacosServiceManager nacosServiceManager,
                                 RedisTemplate redisTemplate) {
        super(nacosServiceManager, nacosDiscoveryProperties);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void register(Registration registration) {
        super.register(registration);
        log.debug("上线的工程名是-{}", applicationName);
        // 发送服务上线事件
        redisTemplate.convertAndSend(ServerConstants.INSTANCE_UP_TOPIC, applicationName);
    }

    @Override
    public void deregister(Registration registration) {
        super.deregister(registration);
        log.debug("下线的工程名是-{}", applicationName);
        // 发送服务下线事件
        redisTemplate.convertAndSend(ServerConstants.INSTANCE_DOWN_TOPIC, applicationName);
    }
}
