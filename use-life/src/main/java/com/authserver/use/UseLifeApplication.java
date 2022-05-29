package com.authserver.use;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authserver.use.mapper",}, annotationClass = Repository.class)
@SpringBootApplication
public class UseLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseLifeApplication.class, args);
    }

}
