package com.authorization.utils.listener;

import cn.hutool.json.JSONUtil;
import com.authorization.utils.json.JsonDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 监听服务是否启动成功
 */
@Slf4j
@Order // @Order注解可以指定组件的加载顺序，数值越小优先级越高。
public class ApplicationEvenListener implements CommandLineRunner, ApplicationRunner, ApplicationListener<ApplicationEvent> {

    @Override
    public void run(String... args) {
        log.warn("CommandLineRunner args:{}", JSONUtil.toJsonStr(args));
        RuntimeMXBean runtimeJvmBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeJvmBean.getUptime();
        String jvmStartTime = JsonDateUtil.formatMillis(uptime);
        log.warn("ApplicationEvenListener CommandLineRunner jvmStartTime: {}: ", jvmStartTime);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.warn("ApplicationArguments args: {}", JSONUtil.toJsonStr(args));
    }

    /**
     * 监听到的SpringBoot事件
     * @param event 事件信息
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.warn("Application event received: {}, eventData:{}", event, event.getSource());
        if (event instanceof ApplicationStartedEvent startedEvent) {
            log.warn("应用启动耗时:{}", JsonDateUtil.formatMillis(startedEvent.getTimeTaken().toMillis()));
        }
        if (event instanceof ApplicationReadyEvent readyEvent) {
            log.warn("应用已完全启动，可以开始处理请求, 完全就绪耗时: {}", JsonDateUtil.formatMillis(readyEvent.getTimeTaken().toMillis()));
        }
    }

}
