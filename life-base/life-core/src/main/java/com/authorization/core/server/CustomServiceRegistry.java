package com.authorization.core.server;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.contsant.ServerOnlineConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;


@Slf4j
public class CustomServiceRegistry extends NacosServiceRegistry {

    private final StringRedisService stringRedisService;

    @Value("${spring.application.name}")
    private String applicationName;

    public CustomServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,
                                 StringRedisService stringRedisService) {
        super(nacosDiscoveryProperties);
        this.stringRedisService = stringRedisService;
    }

    @Override
    public void register(Registration registration) {
        super.register(registration);
        log.debug("上线的工程名是-{}", applicationName);
        // 发送服务上线事件
        stringRedisService.convertAndSend(ServerOnlineConstants.INSTANCE_UP_TOPIC, applicationName);
    }

    @Override
    public void deregister(Registration registration) {
        super.deregister(registration);
        log.debug("下线的工程名是-{}", applicationName);
        // 发送服务下线事件
        stringRedisService.convertAndSend(ServerOnlineConstants.INSTANCE_DOWN_TOPIC, applicationName);
    }
}
