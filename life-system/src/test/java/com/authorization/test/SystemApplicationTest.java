package com.authorization.test;

import com.authorization.life.system.SystemLifeApplication;
import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest(classes = SystemLifeApplication.class)
public class SystemApplicationTest {

    @Resource
    private StringRedisService stringRedisService;
    public static final String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";
    @Autowired
    private RedissonClient client;
    @Autowired
    private RedissonRxClient redissonRxClient;
    @Autowired
    private RedissonReactiveClient redissonReactiveClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ReactiveRedisTemplate reactiveRedisTemplate;

    @Test
    public void preRedisTemp() throws ExecutionException, InterruptedException {
        RAtomicLong longObject = client.getAtomicLong("test_lock");
        // 同步执行方式
        longObject.compareAndSet(3, 401);
        // 异步执行方式
        RFuture<Boolean> result = longObject.compareAndSetAsync(3, 401);
        log.info("result-{}", result);
        log.info("result-{}", result.get());
    }

}
