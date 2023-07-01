package com.authorization.redis.start.config;

import com.authorization.redis.start.listener.RedisSubscription;
import com.authorization.redis.start.lock.DistributedLockService;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.excutor.ExecutorManager;
import com.authorization.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
    public StringRedisService stringRedisService(RedisTemplate<String, String> stringRedisTemplate) {
        log.info("initialed redis-start-life StringRedisService");
        return new StringRedisService(stringRedisTemplate, JsonHelper.getObjectMapper());
    }

    @Bean
    public RedisTemplate<String, Object> authRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
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

    /**
     * 构建分布式锁服务
     * <p>
     * 参考:
     * https://blog.csdn.net/qq_44695727/article/details/115903957
     * http://www.51ufo.cn/%E5%BE%AE%E6%9C%8D%E5%8A%A1/%E5%88%86%E5%B8%83%E5%BC%8F/2020/11/30/SpringBoot%E6%95%B4%E5%90%88Redisson.html
     * https://www.cnblogs.com/east7/p/16255305.html
     * https://www.cnblogs.com/east7/p/16271043.html
     * https://blog.csdn.net/weixin_43410352/article/details/120216509
     */
    @Bean
    public DistributedLockService distributedLockService(RedissonClient redissonClient,
                                                         RedissonReactiveClient redissonReactiveClient) {
        log.info("初始化分布式锁服务...");
        return new DistributedLockService(redissonClient, redissonReactiveClient);
    }


}
