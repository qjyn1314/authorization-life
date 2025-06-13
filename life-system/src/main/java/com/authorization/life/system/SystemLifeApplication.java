package com.authorization.life.system;

import com.authorization.remote.authserver.auto.EnableAuthServerConsumer;
import com.authorization.remote.system.auto.EnableSystemProvider;
import com.authorization.common.exception.ErrorMsgFormat;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

@EnableAuthServerConsumer
@EnableSystemProvider
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authorization.life.system.infra.mapper",}, annotationClass = Repository.class)
@SpringBootApplication
public class SystemLifeApplication {

    public static void main(String[] args) {
        ErrorMsgFormat.addBasenames("classpath:message/messages_system");
        SpringApplication.run(SystemLifeApplication.class, args);
    }

}

