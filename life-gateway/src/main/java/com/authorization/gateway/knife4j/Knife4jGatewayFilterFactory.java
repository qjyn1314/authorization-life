package com.authorization.gateway.knife4j;


import com.authorization.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 文档过滤器, 针对 /v3/api-docs/ 的请求返回值做处理.
 */
@Slf4j
public class Knife4jGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements InitializingBean {

    public static final String KNIFE4J = "Knife4j";

    public static final String V3_DOCS = "/v3/api-docs/";

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public GatewayFilter apply(Object config) {
        // https://www.cnblogs.com/ye-feng-yu/p/11748390.html
        return new ModifyResponseGatewayFilter();
    }

    @Override
    public String name() {
        return KNIFE4J;
    }

    public static class ModifyResponseGatewayFilter implements GatewayFilter, Ordered {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            addOriginalRequestUrl(exchange, request.getURI());
            String path = request.getURI().getRawPath();
            String serviceName = Arrays.stream(StringUtils.tokenizeToStringArray(path, "/")).limit(1).collect(Collectors.joining("/"));
            if (!path.contains(V3_DOCS)) {
                return chain.filter(exchange);
            }
            return chain.filter(exchange.mutate().response(decorate(exchange, serviceName)).build());
        }

        @SuppressWarnings("unchecked")
        ServerHttpResponse decorate(ServerWebExchange exchange, String serviceName) {
            return new ServerHttpResponseDecorator(exchange.getResponse()) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                    Class inClass = String.class;
                    Class outClass = String.class;

                    String originalResponseContentType = exchange.getAttribute(ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                    HttpHeaders httpHeaders = new HttpHeaders();

                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, originalResponseContentType);

                    ClientResponse clientResponse = ClientResponse.create(exchange.getResponse().getStatusCode()).headers(headers -> headers.putAll(httpHeaders)).body(Flux.from(body)).build();

                    Mono modifiedBody = clientResponse.bodyToMono(inClass).flatMap(originalBody -> {
                        Map map = JsonHelper.readValue(originalBody.toString(), Map.class);
                        Object paths = map.get("paths");
                        if (Objects.isNull(paths)) {
                            return Mono.just(originalBody);
                        }
                        Map pathsMap = JsonHelper.readValue(JsonHelper.writeValueAsString(paths), Map.class);
                        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
                        pathsMap.forEach((k, v) -> linkedHashMap.put("/" + serviceName + k, v));
                        map.put("paths", linkedHashMap);
                        return Mono.just(JsonHelper.writeValueAsString(map));
                    });

                    BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
                    CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getResponse().getHeaders());
                    return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
                        Flux<DataBuffer> messageBody = outputMessage.getBody();
                        HttpHeaders headers = getDelegate().getHeaders();
                        if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                            messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                        }
                        return getDelegate().writeWith(messageBody);
                    }));
                }

                @Override
                public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                    return writeWith(Flux.from(body).flatMapSequential(p -> p));
                }
            };
        }

        @Override
        public int getOrder() {
            return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
        }
    }
}
