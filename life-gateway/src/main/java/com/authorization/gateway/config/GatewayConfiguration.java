package com.authorization.gateway.config;

import com.authorization.gateway.execption.ReactiveExceptionHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 网关配置类
 */
@Slf4j
@Configuration
public class GatewayConfiguration {


    @Bean
    public ReactiveExceptionHandler reactiveExceptionHandler() {
        return new ReactiveExceptionHandler();
    }

    /**
     * 配置断路器, 即根据接口的响应, 匹配设置的规则, 达到某一个规则时, 将网关(gateway)与业务(system)服务之间的链接断开.
     * 参考：
     * <p>
     * https://www.cnblogs.com/bolingcavalry/p/15575336.html
     * <p>
     * https://www.jianshu.com/p/29699c3fb65f
     * <p>
     * 重点参考:
     * https://blog.csdn.net/boling_cavalry/article/details/119849436
     * <p>
     * 官网: https://cloud.spring.io/spring-cloud-circuitbreaker/reference/html/spring-cloud-circuitbreaker.html
     *
     * @return ReactiveResilience4JCircuitBreakerFactory
     */
    @Bean
    public ReactiveResilience4JCircuitBreakerFactory r4jFactory(CircuitBreakerRegistry circuitBreakerRegistry,
                                                                TimeLimiterRegistry timeLimiterRegistry,
                                                                Resilience4JConfigurationProperties resilience4JConfigurationProperties) {
        // 断路器配置
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                // 滑动窗口的类型为时间窗口
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                // 时间窗口的大小为60秒
                .slidingWindowSize(60)
                // 在单位时间窗口期内最少需要10次调用才能开始进行统计计算
                .minimumNumberOfCalls(10)
                // 在单位时间窗口内调用失败率达到60%后会启动断路器
                .failureRateThreshold(40)
                // 允许断路器自动由打开状态转换为半开状态
                .enableAutomaticTransitionFromOpenToHalfOpen()
                // 在半开状态下允许进行正常调用的次数
                .permittedNumberOfCallsInHalfOpenState(5)
                // 断路器打开状态转换为半开状态需要等待60秒
                .waitDurationInOpenState(Duration.ofSeconds(20))
                // 当作失败处理的异常类型
                .recordExceptions(Throwable.class)
                .build();
        // 超时配置: timeLimiterConfig方法设置了超时时间，服务提供者如果超过200毫秒没有响应，Spring Cloud Gateway就会向调用者返回失败
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                // 设置超时时间为300毫秒,则进行触发返回
                .timeoutDuration(Duration.ofSeconds(30))
                .build();
        ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory =
                new ReactiveResilience4JCircuitBreakerFactory(circuitBreakerRegistry, timeLimiterRegistry, resilience4JConfigurationProperties);
        circuitBreakerFactory.configureDefault(id ->
                new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(timeLimiterConfig)
                        .circuitBreakerConfig(circuitBreakerConfig)
                        .build());
        return circuitBreakerFactory;
    }
}
