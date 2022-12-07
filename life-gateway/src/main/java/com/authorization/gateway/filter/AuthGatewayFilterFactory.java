package com.authorization.gateway.filter;


import com.authorization.redis.start.service.StringRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * 权限拦截器, 没有此用户权限的路径将抛出403异常.
 */
@Slf4j
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements InitializingBean {

    public static final String AUTH = "Auth";

    @Autowired
    private StringRedisService stringRedisService;

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            return null;
        };
    }

    @Override
    public String name() {
        return AUTH;
    }
}
