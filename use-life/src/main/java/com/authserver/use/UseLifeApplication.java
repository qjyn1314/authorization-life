package com.authserver.use;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UseLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UseLifeApplication.class, args);
    }

}
