package com.authorization.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * 打印路径中的参数信息和请求头
 */
@Slf4j
@Component
public class ListeningGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements InitializingBean, Ordered {

    public static final String LISTENING = "Listening";

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            headers.forEach((key, value) -> {
                log.info("key-->{}--value->>>{}", key, value);
            });
            return chain.filter(exchange);
        };
    }

    @Override
    public String name() {
        return LISTENING;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
