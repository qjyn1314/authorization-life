package com.authorization.test;

import com.authorization.system.SystemLifeApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest(classes = SystemLifeApplication.class)
public class SystemApplicationTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public static final String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";


    @Test
    public void preRedisTemp() {

    }

}
