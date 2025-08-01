package com.authorization.redis.start.config;

import com.authorization.redis.start.listener.RedisSubscription;
import com.authorization.redis.start.service.CaptchaService;
import com.authorization.redis.start.service.impl.CaptchaServiceImpl;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.json.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

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
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTempAutoConfig {

//    @Bean
//    public RedisService redisService(StringRedisTemplate stringRedisTemplate) {
//        log.info("initialed redis-start-life RedisService");
//        return new RedisService(stringRedisTemplate, JsonHelper.getObjectMapper());
//    }

    @Bean
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }

    @Bean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        log.info("initialed redis-start-life RedisUtil");
        return new RedisUtil().setRedisTemplate(stringRedisTemplate).setObjectMapper(JsonHelper.getObjectMapper());
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
                                                                       ScheduledExecutorService scheduledExecutorService) {
        log.info("初始化配置redis发布订阅消息配置类");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(scheduledExecutorService);
        for (RedisSubscription redisSubscriber : redisSubscriberList) {
            log.debug("redisSubscriber->topic-{}-topicName-{}", redisSubscriber.topic(), redisSubscriber.topicName());
            container.addMessageListener(redisSubscriber, redisSubscriber.topic());
        }
        log.info("initialed RedisMessageListenerContainer redisSubscriberList.size ：{}", redisSubscriberList.size());
        return container;
    }

}
