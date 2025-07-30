package com.authorization.gateway.execption;

import com.alibaba.fastjson2.JSON;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.result.Result;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/** 自定义错误信息处理类 */
@Slf4j
@Order(Integer.MIN_VALUE)
@RequiredArgsConstructor
public class ReactiveExceptionHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

    ServerHttpResponse response = exchange.getResponse();

    if (response.isCommitted()) {
      return Mono.error(ex);
    }

    // header set
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    if (ex instanceof ResponseStatusException) {
      response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
    }
    // 当抛出此异常则返回状态码为 401
    if (ex instanceof UnauthorizedException) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    return response.writeWith(
        Mono.fromSupplier(
            () -> {
              DataBufferFactory bufferFactory = response.bufferFactory();
              try {
                log.error("ex-->", ex);
                log.error(
                    "Error Spring Cloud Gateway : {} {}",
                    exchange.getRequest().getPath(),
                    ex.getMessage());
                Result<Object> result = Result.failCode(Result.ERROR, ex.getMessage());
                if (ex instanceof ResponseStatusException
                    && HttpStatus.GATEWAY_TIMEOUT.value()
                        == Objects.requireNonNull(response.getStatusCode()).value()) {
                  result = Result.failCode(Result.ERROR, null);
                }

                return bufferFactory.wrap(JsonHelper.getObjectMapper().writeValueAsBytes(result));
              } catch (Exception e) {
                log.error("Error writing response", ex);
                log.error("Error writing response JsonException", e);
                return bufferFactory.wrap(
                    JSON.toJSONString(Result.failCode(Result.ERROR, null)).getBytes());
              }
            }));
  }
}
