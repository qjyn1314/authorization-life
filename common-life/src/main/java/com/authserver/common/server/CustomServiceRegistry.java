package com.authserver.common.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
public class CustomServiceRegistry extends NacosServiceRegistry {

    public static final String INSTANCE_UP_TOPIC = "auth-server.service.instance.up";
    public static final String INSTANCE_DOWN_TOPIC = "auth-server.service.instance.down";

    private final RedisTemplate redisHelper;

    @Value("${spring.application.name}")
    private String applicationName;

    public CustomServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,
                                 NacosServiceManager nacosServiceManager,
                                 RedisTemplate redisHelper) {
        super(nacosServiceManager, nacosDiscoveryProperties);
        this.redisHelper = redisHelper;
    }

    @Override
    public void register(Registration registration) {
        super.register(registration);
        log.debug("上线的工程名是-{}", applicationName);
        // 发送服务上线事件
        redisHelper.convertAndSend(INSTANCE_UP_TOPIC, applicationName);
    }

    @Override
    public void deregister(Registration registration) {
        super.deregister(registration);
        // 发送服务下线事件
        redisHelper.convertAndSend(INSTANCE_DOWN_TOPIC, applicationName);
    }
}
