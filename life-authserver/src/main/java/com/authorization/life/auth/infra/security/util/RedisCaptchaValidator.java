package com.authorization.life.auth.infra.security.util;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.security.SecurityCoreService;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码校验器
 */
public final class RedisCaptchaValidator {

    private static final String CAPTCHA_CACHE_KEY = SecurityCoreService.CAPTCHA_CACHE_KEY;

    /**
     * 默认时间 10 分钟
     */
    private static final int DEFAULT_EXPIRE = 10;
    private static final int DEFAULT_COUNT = 4;
    private static final int DEFAULT_NUM_COUNT = 4;
    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 32;


    private static final CodeGenerator DEFAULT_GEN = new RandomGenerator(DEFAULT_COUNT);
    private static final CodeGenerator NUM_GEN = new RandomGenerator(RandomUtil.BASE_NUMBER, DEFAULT_NUM_COUNT);


    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil) {
        return create(redisUtil, DEFAULT_GEN, null, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, String uuid) {
        return create(redisUtil, DEFAULT_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建数字验证码, 6位
     *
     * @param redisUtil redisUtil
     * @return Captcha
     */
    public static Captcha createNumCaptcha(RedisUtil redisUtil) {
        return create(redisUtil, NUM_GEN, null, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建数字验证码, 6位
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key
     * @return Captcha
     */
    public static Captcha createNumCaptcha(RedisUtil redisUtil, String uuid) {
        return create(redisUtil, NUM_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_EXPIRE);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key, 为空则会自动生成
     * @param expire    缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, String uuid, long expire) {
        return create(redisUtil, DEFAULT_GEN, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @param baseStr   验证码基础字符集
     * @param uuid      验证码缓存key, 为空则会自动生成
     * @param expire    缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, String baseStr, String uuid, long expire) {
        return create(redisUtil, new RandomGenerator(baseStr, DEFAULT_COUNT), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @param count     验证码长度
     * @param uuid      验证码缓存key, 为空则会自动生成
     * @param expire    缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, int count, String uuid, long expire) {
        count = count <= 0 ? DEFAULT_COUNT : count;
        return create(redisUtil, new RandomGenerator(RandomUtil.BASE_NUMBER, count), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil redisUtil
     * @param baseStr   验证码基础字符集
     * @param count     验证码长度
     * @param uuid      验证码缓存key, 为空则会自动生成
     * @param expire    缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, String baseStr, int count, String uuid, long expire) {
        count = count <= 0 ? DEFAULT_COUNT : count;
        return create(redisUtil, new RandomGenerator(baseStr, count), uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil     redisUtil
     * @param codeGenerator 随机码生成器，空则会使用默认生成器
     * @param uuid          验证码缓存key, 为空则会自动生成
     * @param expire        缓存生存时间，单位: 秒, 传入时间小于0则默认十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, CodeGenerator codeGenerator, String uuid, long expire) {
        return create(redisUtil, codeGenerator, uuid, DEFAULT_WIDTH, DEFAULT_HEIGHT, expire);
    }

    /**
     * 创建验证码
     *
     * @param redisUtil     redisUtil
     * @param codeGenerator 随机码生成器，空则会使用默认生成器
     * @param uuid          验证码缓存key, 为空则会自动生成
     * @param height        验证码高度
     * @param width         验证码宽度
     * @param expire        缓存生存时间，单位: 秒, 传入时间小于0则默认六十分钟
     * @return Captcha
     */
    public static Captcha create(RedisUtil redisUtil, CodeGenerator codeGenerator, String uuid, int width, int height, long expire) {
        Assert.notNull(redisUtil, "redisUtil is null!");
        expire = expire <= 0 ? DEFAULT_EXPIRE : expire;
        codeGenerator = Objects.isNull(codeGenerator) ? DEFAULT_GEN : codeGenerator;
        GifCaptcha shearCaptcha = CaptchaUtil.createGifCaptcha(width, height);
        shearCaptcha.setGenerator(codeGenerator);
        uuid = StrUtil.isBlank(uuid) ? UUID.fastUUID().toString(true) : uuid;
        redisUtil.setEx(StrUtil.format(CAPTCHA_CACHE_KEY, Map.of("uuid", uuid)), shearCaptcha.getCode(), expire, TimeUnit.MINUTES);
        return Captcha.of(shearCaptcha, uuid);
    }

    /**
     * 校验验证码
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key
     * @param code      待验证的验证码
     * @return 通过验证为true，未通过为false
     */
    public static boolean verify(RedisUtil redisUtil, String uuid, String code) {
        Assert.notNull(redisUtil, "redisUtil is null!");
        if (StrUtil.isBlank(uuid) && StrUtil.isBlank(code)) {
            return false;
        }
        String verifyKey = StrUtil.format(CAPTCHA_CACHE_KEY, Map.of("uuid", uuid));
        String okValue = redisUtil.get(verifyKey);
        return StrUtil.equalsIgnoreCase(code, okValue);
    }

    /**
     * 校验验证码有效期
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key
     * @param code      待验证的验证码
     * @return 通过验证为true，未通过为false
     */
    public static boolean verifyExpirationDate(RedisUtil redisUtil, String uuid, String code) {
        Assert.notNull(redisUtil, "redisUtil is null!");
        if (StrUtil.isBlank(uuid) && StrUtil.isBlank(code)) {
            return false;
        }
        return Objects.nonNull(redisUtil.get(StrUtil.format(CAPTCHA_CACHE_KEY, Map.of("uuid", uuid))));
    }

    /**
     * 校验验证码是否重复发送
     *
     * @param redisUtil redisUtil
     * @param uuid      验证码缓存key
     * @return 通过验证为true，未通过为false
     */
    public static boolean verifyRepeatPictureCode(RedisUtil redisUtil, String uuid) {
        Assert.notNull(redisUtil, "redisUtil is null!");
        if (StrUtil.isBlank(uuid)) {
            return false;
        }
        return Objects.nonNull(redisUtil.get(StrUtil.format(CAPTCHA_CACHE_KEY, Map.of("uuid", uuid))));
    }

}
