package com.authorization.life.system;

import com.authorization.remote.system.auto.EnableSystemConsumer;
import com.authorization.remote.system.auto.EnableSystemProvider;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

@EnableSystemConsumer
@EnableSystemProvider
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authserver.system.mapper",}, annotationClass = Repository.class)
@SpringBootApplication
public class SystemLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemLifeApplication.class, args);
    }

}
