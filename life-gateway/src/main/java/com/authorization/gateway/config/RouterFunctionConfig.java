package com.authorization.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

/**
 * 自定义的请求路由配置
 */
@Slf4j
@Configuration
public class RouterFunctionConfig {

    /**
     * 访问根目录时将重定向到登录模块
     *
     * @return 登录模块
     */
    @Bean
    public RouterFunction<ServerResponse> loginRouterFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/"),
                request -> ServerResponse.temporaryRedirect(URI.create(request.uri() + "login")).build());
    }

}
