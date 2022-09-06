package com.authorization.redis.start;

import cn.hutool.json.JSONUtil;
import com.authorization.start.util.excutor.ExecutorManager;
import com.authorization.start.util.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * redis 配置类
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/28 10:26
 */
@Slf4j
@Configuration
public class LifeRedisAutoConfiguration {

    @Bean
    @Primary
    @Order(Integer.MIN_VALUE)
    @ConditionalOnProperty("spring.redis.host")
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<String, String> lifeRedisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("RedisTemplate Init ...");
        RedisTemplate<String, String> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = ObjectMappers.configMapper();
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setStringSerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        log.info("RedisTemplate Init ...-{}", JSONUtil.toJsonStr(template));
        return template;
    }

    /**
     * 配置redis监听消息配置类
     *
     * @param connectionFactory   链接参数
     * @param redisSubscriberList 监听列表
     * @return RedisMessageListenerContainer
     */
    @Bean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       List<RedisSubscription> redisSubscriberList) {
        log.info("初始化-redisMessageListenerContainer-connectionFactory-{}", JSONUtil.toJsonStr(connectionFactory));
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(scheduledRedisListenerExecutor());
        for (RedisSubscription redisSubscriber : redisSubscriberList) {
            container.addMessageListener(redisSubscriber, redisSubscriber.topic());
        }
        return container;
    }

    /**
     * 设置线程池的目的是需要让定时任务不浪费主线程的情况下，尽最大努力的去执行任务，如果执行到了拒绝策略，那么交给调用方执行此任务。
     * <p>
     * 拒绝策略：jdk中自带的 ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     * <p>
     * 线程池中线程名字：spring自带的  CustomizableThreadFactory
     * <p>
     * 参考：
     * <p>
     * jdk自带的拒绝策略详解：https://blog.csdn.net/suifeng629/article/details/98884972
     * <p>
     * 第三方框架对于拒绝策略的实现(例如dubbo、activemq、netty、)：http://www.kailing.pub/article/index/arcid/255.html
     * <p>
     * 线程池中的线程名字：https://blog.csdn.net/u010648555/article/details/106137206
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService scheduledRedisListenerExecutor() {
        log.debug("Init scheduledRedisListenerExecutor corePoolSize ：{}", ExecutorManager.getCpuProcessors());
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(ExecutorManager.getCpuProcessors(),
                new CustomizableThreadFactory(SCHEDULED_TASKS_NAME),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ExecutorManager.displayThreadPoolStatus(scheduledThreadPoolExecutor, SCHEDULED_TASKS_NAME);
        return scheduledThreadPoolExecutor;
    }

    /**
     * 定时任务的线程池名称
     */
    private static final String SCHEDULED_TASKS_NAME = "REDIS-LISTENER-TASK-";

}
