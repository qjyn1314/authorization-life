package com.authorization.redis.start.model;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 *
 * @author wangjunming
 * @since 2025-07-09 15:34
 */
@Setter
@Getter
public class RedisCaptcha implements Serializable {

    /**
     * 生成验证码的key示例格式: sso-oauth-server:auth:captcha-code:{bizType}:{bizId}:{uuid}
     */
    public static final String sso_oauth_server = "sso-oauth-server:captcha-code:%s:%s:%s";

    /** 业务类型 */
    private String bizType;

    /** 业务ID */
    private String bizId;

    /** uuid */
    private String uuid;

    /** 验证码 */
    private String code;

    /** 过期时间(单位:分钟) */
    private Integer expiredTime = 10;

    /** 格式字符串 */
    private String format;

    /**
     * 如果为空则返回默认的格式
     */
    public String getFormat() {
        return StrUtil.isBlank(this.format) ? sso_oauth_server : this.format;
    }

    /** 格式化后的key */
    private String key;

    public String getKey() {
        if (StrUtil.isNotBlank(this.bizType) && StrUtil.isNotBlank(this.bizId) && StrUtil.isNotBlank(this.uuid)) {
            return StrUtil.isBlank(this.key) && sso_oauth_server.equals(this.getFormat()) ? String.format(sso_oauth_server, this.bizType, this.bizId, this.uuid) : this.key;
        }
        return this.key;
    }

    /** 验证码图片信息 */
    private Captcha captcha;

    private RedisCaptcha() {
    }

    public static RedisCaptcha ofKeyUuid(String key, Integer expiredTime) {
        RedisCaptcha redisCaptcha = new RedisCaptcha();
        redisCaptcha.setKey(key);
        redisCaptcha.setExpiredTime(expiredTime);
        return redisCaptcha;
    }

    public static RedisCaptcha of(String bizType, String bizId) {
        return of(bizType, bizId, UUID.fastUUID().toString(true));
    }

    public static RedisCaptcha of(String bizType, String bizId, String uuid) {
        RedisCaptcha redisCaptcha = new RedisCaptcha();
        redisCaptcha.setBizType(bizType);
        redisCaptcha.setBizId(bizId);
        if (StrUtil.isNotBlank(uuid)) {
            redisCaptcha.setUuid(uuid);
        } else {
            redisCaptcha.setUuid(UUID.fastUUID().toString(true));
        }
        return redisCaptcha;
    }

    public static RedisCaptcha of(String bizType, String bizId, String uuid, Integer expiredTime) {
        RedisCaptcha redisCaptcha = of(bizType, bizId, uuid);
        redisCaptcha.setExpiredTime(expiredTime);
        return redisCaptcha;
    }

    public static RedisCaptcha of(String bizType, String bizId, String uuid, String format) {
        RedisCaptcha redisCaptcha = of(bizType, bizId, uuid);
        redisCaptcha.setFormat(format);
        return redisCaptcha;
    }

    private static final int NUM_GEN_COUNT = 6;
    private static final CodeGenerator NUM_GEN = new RandomGenerator(RandomUtil.BASE_NUMBER, NUM_GEN_COUNT);


    public RedisCaptcha genCaptcha() {
        return genCaptcha(120, 30);
    }

    public RedisCaptcha genCaptcha(int width, int height) {
        GifCaptcha shearCaptcha = CaptchaUtil.createGifCaptcha(width, height);
        shearCaptcha.setGenerator(NUM_GEN);
        this.code = shearCaptcha.getCode();
        this.captcha = Captcha.of(shearCaptcha, this.uuid);
        return this;
    }

}
