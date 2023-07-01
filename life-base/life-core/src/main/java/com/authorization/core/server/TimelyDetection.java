package com.authorization.core.server;

import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.contsant.ServerUpDown;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

/**
 *  redis 发布 当前服务上线事件
 */
@Slf4j
public class TimelyDetection implements CommandLineRunner {

    private final StringRedisService stringRedisService;

    @Value("${spring.application.name}")
    private String applicationName;

    public TimelyDetection(StringRedisService stringRedisService) {
        this.stringRedisService = stringRedisService;
    }

    /**
     * 在项目启动成功之后会立即执行, 此服务在gateway中的route路由的刷新.
     */
    @Override
    public void run(String... args) {
        log.info("上线的工程名是-{}", applicationName);
        // 发送服务上线事件
        stringRedisService.convertAndSend(ServerUpDown.INSTANCE_UP_TOPIC, applicationName);
    }

}
