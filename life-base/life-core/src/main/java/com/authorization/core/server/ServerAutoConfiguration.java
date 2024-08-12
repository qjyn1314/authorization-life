package com.authorization.core.server;

import com.authorization.redis.start.util.RedisService;
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
@Import(RedisService.class)
public class ServerAutoConfiguration {

    @Bean
    @Order
    public TimelyDetection timelyDetection(RedisService stringRedisService) {
        return new TimelyDetection(stringRedisService);
    }

}
