package com.authorization.life.auth.infra.security.util;

import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.redis.start.util.RedisService;
import com.authorization.utils.message.StrForm;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码校验器
 */
public final class RedisCaptchaValidator {

    private static final String CAPTCHA_CACHE_KEY = "auth-server:valid:captcha-code:{uuid}";

    /**
     * 默认时间 60 分钟
     */
    private static final int DEFAULT_EXPIRE = 60;
    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_NUM_COUNT = 6;
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 32;


    private static final CodeGenerator DEFAULT_GEN = new RandomGenerator(DEFAULT_COUNT);
    private static final CodeGenerator NUM_GEN = new RandomGenerator(RandomUtil.BASE_NUMBER, DEFAULT_NUM_COUNT);


    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService) {
        return create(stringRedisService, new RandomGenerator(RandomUtil.BASE_NUMBER, DEFAULT_COUNT), null, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @param uuid        验证码缓存key
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, String uuid) {
        return create(stringRedisService, DEFAULT_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建数字验证码, 6位
     *
     * @param stringRedisService stringRedisService
     * @param uuid        验证码缓存key
     * @return Captcha
     */
    public static Captcha createNumCaptcha(RedisService stringRedisService, String uuid) {
        return create(stringRedisService, NUM_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }


    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @param uuid        验证码缓存key, 为空则会自动生成
     * @param expire      缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, String uuid, long expire) {
        return create(stringRedisService, DEFAULT_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @param baseStr     验证码基础字符集
     * @param uuid        验证码缓存key, 为空则会自动生成
     * @param expire      缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, String baseStr, String uuid, long expire) {
        return create(stringRedisService, new RandomGenerator(baseStr, DEFAULT_COUNT), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @param count       验证码长度
     * @param uuid        验证码缓存key, 为空则会自动生成
     * @param expire      缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, int count, String uuid, long expire) {
        count = count <= 0 ? DEFAULT_COUNT : count;
        return create(stringRedisService, new RandomGenerator(RandomUtil.BASE_NUMBER, count), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService stringRedisService
     * @param baseStr     验证码基础字符集
     * @param count       验证码长度
     * @param uuid        验证码缓存key, 为空则会自动生成
     * @param expire      缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, String baseStr, int count, String uuid, long expire) {
        count = count <= 0 ? DEFAULT_COUNT : count;
        return create(stringRedisService, new RandomGenerator(baseStr, count), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService   stringRedisService
     * @param codeGenerator 随机码生成器，空则会使用默认生成器
     * @param uuid          验证码缓存key, 为空则会自动生成
     * @param expire        缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, CodeGenerator codeGenerator, String uuid, long expire) {
        return create(stringRedisService, codeGenerator, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param stringRedisService   stringRedisService
     * @param codeGenerator 随机码生成器，空则会使用默认生成器
     * @param uuid          验证码缓存key, 为空则会自动生成
     * @param height        验证码高度
     * @param width         验证码宽度
     * @param expire        缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisService stringRedisService, CodeGenerator codeGenerator, String uuid, int width, int height, long expire) {
        Assert.notNull(stringRedisService, "redisHelper is null!");
        expire = expire <= 0 ? DEFAULT_EXPIRE : expire;
        codeGenerator = Objects.isNull(codeGenerator) ? DEFAULT_GEN : codeGenerator;
        ShearCaptcha shearCaptcha = new ShearCaptcha(width, height);
        shearCaptcha.setGenerator(codeGenerator);
        uuid = StrUtil.isBlank(uuid) ? UUID.fastUUID().toString(true) : uuid;
        stringRedisService.strSet(StrForm.of(CAPTCHA_CACHE_KEY).add("uuid", uuid).format(), shearCaptcha.getCode(),
                expire, TimeUnit.MINUTES);
        return Captcha.of(shearCaptcha, uuid);
    }

    /**
     * 校验验证码
     *
     * @param stringRedisService stringRedisService
     * @param uuid        验证码缓存key
     * @param code        待验证的验证码
     * @return 通过验证为true，未通过为false
     */
    public static boolean verify(RedisService stringRedisService, String uuid, String code) {
        String okValue = stringRedisService.strGet(StrForm.of(CAPTCHA_CACHE_KEY).add("uuid", uuid).format());
        return StrUtil.equalsIgnoreCase(code, okValue);
    }
}
