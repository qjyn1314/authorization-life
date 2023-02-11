package com.authorization.test;

import com.authorization.life.system.SystemLifeApplication;
import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest(classes = SystemLifeApplication.class)
public class SystemApplicationTest {

    @Resource
    private StringRedisService stringRedisService;
    public static final String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedissonRxClient redissonRxClient;
    @Autowired
    private RedissonReactiveClient redissonReactiveClient;
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    private ReactiveRedisTemplate<String, String> stringReactiveRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> authRedisTemplate;

    @Test
    public void preRedisTemp() {
    }

}
