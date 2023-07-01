package com.authorization.core.server;

import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * 需要刷新服务
 *
 * @author wangjunming
 */
@Slf4j
@AutoConfiguration
@Import(StringRedisService.class)
public class ServerAutoConfiguration {

    @Bean
    @Order
    public TimelyDetection timelyDetection(StringRedisService stringRedisService) {
        return new TimelyDetection(stringRedisService);
    }

}
