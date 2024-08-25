package com.authorization.gateway.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 截取服务名称并移除这一片段
 * eg. /authserver-life/auth/login ->
 * Service-Name: authserver-life
 * uri: /auth/login
 */
@Slf4j
@Component
public class UrlResolveGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    public static final String URL_RESOLVE = "UrlResolve";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            addOriginalRequestUrl(exchange, request.getURI());
            String path = request.getURI().getRawPath();
            String httpMethod = request.getMethod().toString();
            String serviceName = Arrays.stream(StringUtils.tokenizeToStringArray(path, "/")).limit(1).collect(Collectors.joining("/"));
            String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(path, "/")).skip(1).collect(Collectors.joining("/"));
            newPath += (newPath.length() > 1 && path.endsWith("/") ? "/" : "");
            final String finalNewPath = newPath;
            ServerHttpRequest newRequest = request.mutate().path(newPath).build();
            log.info("request url: {}", path);
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR,
                    newRequest.getURI());
            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    @Override
    public String name() {
        return URL_RESOLVE;
    }
}
