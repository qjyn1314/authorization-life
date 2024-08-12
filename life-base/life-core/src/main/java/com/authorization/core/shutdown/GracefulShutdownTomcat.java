package com.authorization.core.shutdown;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import com.authorization.redis.start.util.RedisService;
import com.authorization.utils.contsant.ServerUpDown;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * redis 用于发送当前服务关闭消息
 */
@Slf4j
public class GracefulShutdownTomcat implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private volatile Connector connector;
    private final int waitTime = 30;
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    private final RedisService stringRedisService;

    @Value("${spring.application.name}")
    private String applicationName;

    public GracefulShutdownTomcat(NacosAutoServiceRegistration nacosAutoServiceRegistration, RedisService stringRedisService) {
        this.nacosAutoServiceRegistration = nacosAutoServiceRegistration;
        this.stringRedisService = stringRedisService;
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.info("[Nacos] deregister hook now.");
        // nacos解除注册
        nacosAutoServiceRegistration.stop();
        // 发送服务下线事件
        stringRedisService.convertAndSend(ServerUpDown.INSTANCE_DOWN_TOPIC, applicationName);
        log.info("[Tomcat] Run shutdown hook now.");
        this.connector.pause();
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                    log.warn("Tomcat thread pool did not shut down gracefully within " + waitTime + " seconds. Proceeding with forceful shutdown");
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
