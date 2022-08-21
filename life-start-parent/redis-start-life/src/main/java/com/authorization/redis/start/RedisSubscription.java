package com.authorization.redis.start;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;

import java.nio.charset.StandardCharsets;

/**
 * redis订阅器
 */
public interface RedisSubscription extends MessageListener {

    @Override
    default void onMessage(Message message, byte[] pattern) {
        String topicName = new String(message.getChannel(), StandardCharsets.UTF_8);
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        subscribe(topicName, messageBody);
    }

    default Topic topic(){
        return new ChannelTopic(topicName());
    }

    void subscribe(String topic, String message);

    String topicName();

}
