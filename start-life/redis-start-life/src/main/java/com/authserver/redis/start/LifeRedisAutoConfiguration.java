package com.authserver.redis.start;

import cn.hutool.json.JSONUtil;
import com.authserver.start.json.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
    @Qualifier
    @ConditionalOnProperty("spring.redis.host")
    public RedisTemplate<String, Object> lifeRedisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("RedisTemplate Init ...");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
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
        log.info("RedisTemplate Inited ...-{}", JSONUtil.toJsonStr(template));
        return template;
    }

}
