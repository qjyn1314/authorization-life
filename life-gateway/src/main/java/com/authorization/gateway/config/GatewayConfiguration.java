package com.authorization.gateway.config;

import com.authorization.gateway.execption.ReactiveExceptionHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.filter.factory.DedupeResponseHeaderGatewayFilterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.net.URI;
import java.time.Duration;
import java.util.stream.Collectors;

/**
 * 网关配置类
 */
@Configuration
@AutoConfiguration(after = WebFluxAutoConfiguration.EnableWebFluxConfiguration.class)
public class GatewayConfiguration {

    private ServerProperties serverProperties;

    public GatewayConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    public ServerCodecConfigurer serverCodecConfigurer() {
        return new DefaultServerCodecConfigurer();
    }

    @Bean
    public ServerProperties serverProperties() {
        return new ServerProperties();
    }

    @Bean
    public WebProperties webProperties() {
        return new WebProperties();
    }

    @Bean
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes,
                                                             WebProperties webProperties,
                                                             ObjectProvider<ViewResolver> viewResolvers,
                                                             ServerCodecConfigurer serverCodecConfigurer,
                                                             ApplicationContext applicationContext) {
        ReactiveExceptionHandler exceptionHandler = new ReactiveExceptionHandler(errorAttributes,
                webProperties, this.serverProperties.getError(), applicationContext);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    /**
     * 访问根目录重定向到登录模块
     *
     * @return 登录模块
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/"),
                request -> ServerResponse.temporaryRedirect(URI.create(request.uri() + "/login")).build());
    }

    /**
     * 配置断路器
     *
     * @return ReactiveResilience4JCircuitBreakerFactory
     */
    @Bean
    public ReactiveResilience4JCircuitBreakerFactory r4jFactory(CircuitBreakerRegistry circuitBreakerRegistry,
                                                                TimeLimiterRegistry timeLimiterRegistry) {
        // 断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 滑动窗口的类型为时间窗口
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                // 时间窗口的大小为60秒
                .slidingWindowSize(60)
                // 在单位时间窗口内最少需要10次调用才能开始进行统计计算
                .minimumNumberOfCalls(10)
                // 在单位时间窗口内调用失败率达到60%后会启动断路器
                .failureRateThreshold(60)
                // 允许断路器自动由打开状态转换为半开状态
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // 在半开状态下允许进行正常调用的次数
                .permittedNumberOfCallsInHalfOpenState(5)
                // 断路器打开状态转换为半开状态需要等待60秒
                .waitDurationInOpenState(Duration.ofSeconds(60))
                // 当作失败处理的异常类型
                // .recordExceptions(SocketException.class)
                .build();
        // 超时配置
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                // 设置超时时间为60s
                .timeoutDuration(Duration.ofSeconds(60))
                .build();
        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory =
                new ReactiveResilience4JCircuitBreakerFactory(circuitBreakerRegistry, timeLimiterRegistry);
        reactiveResilience4JCircuitBreakerFactory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig).build());
        return reactiveResilience4JCircuitBreakerFactory;
    }
}
