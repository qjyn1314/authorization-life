package com.authorization.system.test_redis;

import com.authorization.redis.start.RedisSubscription;
import com.authorization.system.log.LogAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

/**
 * 进行订阅Topic频道
 */
@Slf4j
@Component
public class RedisTestSubscriber implements RedisSubscription {

    @LogAdvice(name = "进行订阅的Topic频道" + RedisTestConstants.REDIS_TEST_TOPIC_)
    @Override
    public void subscribe(String topicName, String messageBody) {
        log.info("订阅的Topic频道为: [{}], 内容为： [{}] ", topicName, messageBody);
    }

    @Override
    public String topicName() {
        return RedisTestConstants.REDIS_TEST_TOPIC_;
    }

    @Override
    public Topic topic() {
        return new PatternTopic(topicName());
    }

}
