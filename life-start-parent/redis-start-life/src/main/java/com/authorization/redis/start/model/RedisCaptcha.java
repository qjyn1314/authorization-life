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

    /**
     * 如果为空则返回默认的格式
     */
    public String getFormat() {
        return sso_oauth_server;
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

    public static RedisCaptcha of(String bizType, String bizId) {
        return of(bizType, bizId, UUID.fastUUID().toString(true));
    }

    public static RedisCaptcha of(String bizType, String bizId, String uuid) {
        return of(bizType, bizId, uuid, defaultExpiredTime);
    }

    public static RedisCaptcha of(String bizType, String bizId, String uuid, Integer expiredTime) {
        RedisCaptcha redisCaptcha = new RedisCaptcha();
        redisCaptcha.setBizType(bizType);
        redisCaptcha.setBizId(bizId);
        if (StrUtil.isNotBlank(uuid)) {
            redisCaptcha.setUuid(uuid);
        } else {
            redisCaptcha.setUuid(UUID.fastUUID().toString(true));
        }
        redisCaptcha.setExpiredTime(expiredTime);
        return redisCaptcha;
    }

    public static RedisCaptcha ofKey(String key, Integer expiredTime) {
        RedisCaptcha redisCaptcha = new RedisCaptcha();
        redisCaptcha.setKey(key);
        redisCaptcha.setExpiredTime(expiredTime);
        return redisCaptcha;
    }

    public static RedisCaptcha ofKey(String key) {
        RedisCaptcha redisCaptcha = new RedisCaptcha();
        redisCaptcha.setKey(key);
        return redisCaptcha;
    }

    private static final Integer defaultExpiredTime = 10;
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

    public interface CaptchaType {
        /**
         * 发送邮箱注册验证码
         */
        String SEND_EMAIL_CODE_REGISTER = "send-email-code-register";
        /**
         * 发送邮箱重置密码验证码
         */
        String SEND_EMAIL_CODE_RESET_PWD = "send-email-code-reset-pwd";
        /**
         * 获取图片验证码
         */
        String VALID_PICTURE_CODE = "valid-picture-code";
        /**
         * 发送手机登录验证码
         */
        String SEND_SMS_CODE_LOGIN = "send-sms-code-login";
        /**
         * 发送手机登录验证码
         */
        String SEND_EMAIL_CODE_LOGIN = "send-email-code-login";
    }

}
