package com.authorization.redis.start.config;

import com.authorization.redis.start.listener.RedisSubscription;
import com.authorization.redis.start.util.RedisService;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.excutor.ExecutorManager;
import com.authorization.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
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
@AutoConfiguration
public class RedisTempAutoConfig {

    @Bean
    public RedisService redisService(StringRedisTemplate stringRedisTemplate) {
        log.info("initialed redis-start-life RedisService");
        return new RedisService(stringRedisTemplate, JsonHelper.getObjectMapper());
    }

    @Bean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        log.info("initialed redis-start-life RedisUtil");
        return new RedisUtil().setRedisTemplate(stringRedisTemplate);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        log.info("initialed redis-start-life RedisTemplate");
        return redisTemplate;
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
        log.info("initialed scheduledRedisListenerExecutor corePoolSize ：{}", ExecutorManager.getCpuProcessors());
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(ExecutorManager.getCpuProcessors(),
                new CustomizableThreadFactory(REDIS_LISTENER_TASKS_NAME),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ExecutorManager.registerAndMonitorThreadPoolExecutor(REDIS_LISTENER_TASKS_NAME, scheduledThreadPoolExecutor);
        return scheduledThreadPoolExecutor;
    }

    /**
     * 定时任务的线程池名称
     */
    public static final String REDIS_LISTENER_TASKS_NAME = "REDIS-LISTENER-TASK-";


    /**
     * 配置redis监听消息配置类
     *
     * @param connectionFactory   链接参数
     * @param redisSubscriberList 监听列表
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       List<RedisSubscription> redisSubscriberList,
                                                                       ScheduledExecutorService scheduledRedisListenerExecutor) {
        log.info("初始化配置redis发布订阅消息配置类");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(scheduledRedisListenerExecutor);
        for (RedisSubscription redisSubscriber : redisSubscriberList) {
            log.debug("redisSubscriber->topic-{}-topicName-{}", redisSubscriber.topic(), redisSubscriber.topicName());
            container.addMessageListener(redisSubscriber, redisSubscriber.topic());
        }
        log.info("initialed RedisMessageListenerContainer redisSubscriberList.size ：{}", redisSubscriberList.size());
        return container;
    }

}
