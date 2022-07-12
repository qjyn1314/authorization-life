package com.authserver.oauth;


import com.authserver.common.security.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Repository;

@EnableDiscoveryClient
@MapperScan(basePackages = {"com.authserver.life.mapper",}, annotationClass = Repository.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OauthLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthLifeApplication.class, args);
    }

}