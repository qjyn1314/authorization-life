package com.authorization.life;

import com.authorization.core.security.config.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/21 20:30
 */
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authorization.life.mapper",}, annotationClass = Repository.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuthServerLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerLifeApplication.class, args);
    }

}
