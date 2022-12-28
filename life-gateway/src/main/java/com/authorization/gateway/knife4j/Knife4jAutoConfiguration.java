package com.authorization.gateway.knife4j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 文档分组配置类
 *
 * @author wangjunming
 * @date 2022/12/20 20:40
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Knife4jProperties.class)
@ConditionalOnProperty(name = "knife4j.gateway.enable", havingValue = "true")
public class Knife4jAutoConfiguration {

    /**
     * 分组url
     */
    public static final String GATEWAY_SWAGGER_GROUP_URL = "/swagger-resources";

    @Bean
    public RouterFunction<ServerResponse> gatewaySwaggerRouteFunc(Knife4jProperties knife4jProperties) {
        log.info("init gateway swagger resources.");
        return RouterFunctions.route().GET(GATEWAY_SWAGGER_GROUP_URL, request ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(knife4jProperties.build())).build();
    }

}
