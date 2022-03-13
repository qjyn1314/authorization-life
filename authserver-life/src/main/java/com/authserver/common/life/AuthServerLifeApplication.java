package com.authserver.common.life;

import com.authserver.common.life.security.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 *   启动类
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/21 20:30
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuthServerLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerLifeApplication.class, args);
    }

}
