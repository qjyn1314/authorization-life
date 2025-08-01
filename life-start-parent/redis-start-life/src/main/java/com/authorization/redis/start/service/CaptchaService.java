package com.authorization.redis.start.service;

import com.authorization.redis.start.model.RedisCaptcha;

/**
 * @author wangjunming
 * @since 2025-07-09 15:21
 */
public interface CaptchaService {

    /**
     * 生成数字的图片验证码
     */
    RedisCaptcha genNumCaptcha(RedisCaptcha captcha);

    boolean validCaptcha(RedisCaptcha captcha, String value);

    void clearCaptcha(RedisCaptcha captcha);

    boolean validClearCaptcha(RedisCaptcha captcha, String value);
}
