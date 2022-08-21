package com.authorization.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * gateway-网关
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/23 14:58
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayLifeApplication.class, args);
    }

}
