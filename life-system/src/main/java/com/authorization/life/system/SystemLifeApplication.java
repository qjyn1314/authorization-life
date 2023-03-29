package com.authorization.life.system;

import com.authorization.life.datasource.start.datasource.annotion.EnableDynamicDataSource;
import com.authorization.remote.authserver.auto.EnableAuthServerConsumer;
import com.authorization.remote.sharding.auto.EnableShardingProvider;
import com.authorization.remote.system.auto.EnableSystemProvider;
import com.authorization.utils.message.MsgResource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

@EnableShardingProvider
@EnableAuthServerConsumer
@EnableSystemProvider
@EnableDynamicDataSource
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authorization.life.system.infra.mapper",}, annotationClass = Repository.class)
@SpringBootApplication
public class SystemLifeApplication {

    public static void main(String[] args) {
        MsgResource.addBasenames("classpath:message/messages_system");
        SpringApplication.run(SystemLifeApplication.class, args);
    }

}
