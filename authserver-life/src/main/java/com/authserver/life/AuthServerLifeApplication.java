package com.authserver.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * <p>
 *   启动类
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/21 20:30
 */
@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class AuthServerLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerLifeApplication.class, args);
    }

}
