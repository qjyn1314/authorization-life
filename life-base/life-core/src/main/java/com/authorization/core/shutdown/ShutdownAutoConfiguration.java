package com.authorization.core.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 服务关闭配置
 */
public class ShutdownAutoConfiguration {

    @Resource
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @Bean
    public GracefulShutdownTomcat gracefulShutdownTomcat() {
        return new GracefulShutdownTomcat(nacosAutoServiceRegistration);
    }
}
