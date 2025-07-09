package com.authorization.life.system.test.test_redis;

import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.json.JsonHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * https://www.jianshu.com/p/02fcbddafbdf
 *
 * @author wangjunming
 * @date 2023/5/10 13:39
 */
@Slf4j
@Getter
@Setter
public class RedisStaticCli {

  @Getter private static String host = "127.0.0.1";
  @Getter private static Integer port = 12030;
  @Getter private static String password = "123456";
  @Getter private static Integer database = 0;

  public static void setHost(String host) {
    RedisStaticCli.host = host;
  }

  public static void setPort(Integer port) {
    RedisStaticCli.port = port;
  }

  public static void setPassword(String password) {
    RedisStaticCli.password = password;
  }

  public static void setDatabase(Integer database) {
    RedisStaticCli.database = database;
  }

  private RedisStaticCli() {}

  public static RedisUtil redis() {
    // 单机模式
    RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
    rsc.setPort(port);
    rsc.setPassword(password);
    rsc.setHostName(host);
    rsc.setDatabase(database);
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(rsc);
    connectionFactory.afterPropertiesSet();
    StringRedisTemplate redisTemplate = getStringRedisTemplate(connectionFactory);
    log.info("init RedisUtil");
    return new RedisUtil().setRedisTemplate(redisTemplate);
  }

  private static StringRedisTemplate getStringRedisTemplate(
      LettuceConnectionFactory connectionFactory) {
    StringRedisTemplate redisTemplate = new StringRedisTemplate();
    redisTemplate.setConnectionFactory(connectionFactory);
    Jackson2JsonRedisSerializer<Object> jacksonSerializer =
        new Jackson2JsonRedisSerializer<>(JsonHelper.getObjectMapper(), Object.class);
    redisTemplate.setValueSerializer(jacksonSerializer);
    redisTemplate.setHashValueSerializer(jacksonSerializer);

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
