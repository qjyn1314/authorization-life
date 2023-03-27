package com.authorization.life.shardingsphere;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authorization.life.shardingsphere.infra.mapper",}, annotationClass = Repository.class)
@SpringBootApplication
public class ShardingLifeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingLifeApplication.class, args);
    }
}