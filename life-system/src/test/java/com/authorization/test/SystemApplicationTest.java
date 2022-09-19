package com.authorization.test;

import com.authorization.redis.start.util.ObjRedisHelper;
import com.authorization.system.SystemLifeApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = SystemLifeApplication.class)
public class SystemApplicationTest {

    @Autowired
    private ObjRedisHelper objRedisHelper;
    public static final String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";


    @Test
    public void preRedisTemp() {

        objRedisHelper.hSet(AUTHORIZATION,"98765431321741852369852147","");

    }

}
