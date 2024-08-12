package com.authorization.redis.start.constant;

/**
 * redis的常量类
 */
public class RedisConstant {

    /**
     * 默认过期时长，单位：秒
     */
    public static final long DEFAULT_EXPIRE = 60 * 60 * 24L;

    /**
     * 不设置过期时长
     */
    public static final long NOT_EXPIRE = -1;

    /**
     * 空字符串
     */
    public static final String STR_EMPTY = "";
}
