package com.authorization.redis.start.util;

import net.dreamlu.mica.redis.cache.MicaRedisCache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 存储对象的redis工具类
 */
public class ObjRedisHelper extends MicaRedisCache {

    public ObjRedisHelper(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }
}
