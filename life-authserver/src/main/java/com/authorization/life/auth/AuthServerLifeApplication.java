package com.authorization.life.auth;

import com.authorization.life.security.start.config.SecurityAutoConfiguration;
import com.authorization.remote.authserver.auto.EnableAuthServerProvider;
import com.authorization.remote.system.auto.EnableSystemConsumer;
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
@EnableSystemConsumer
@EnableAuthServerProvider
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authorization.life.auth.infra.mapper",}, annotationClass = Repository.class)
// 防止SecurityFilterChain 重复声明, 此处将start中的security配置(start中的配置是给其他web工程使用.例如: system)移除, 此时 authserver工程中仅有自身配置的securtiy.
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AuthServerLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerLifeApplication.class, args);
    }

}
