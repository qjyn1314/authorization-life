package com.authorization.redis.start.service.impl;

import cn.hutool.core.util.StrUtil;
import com.authorization.redis.start.model.RedisCaptcha;
import com.authorization.redis.start.service.CaptchaService;
import com.authorization.redis.start.util.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjunming
 * @since 2025-07-09 15:21
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public RedisCaptcha genNumCaptcha(RedisCaptcha captcha) {
        String captchaKey = captcha.getKey();
        String value = redisUtil.get(captchaKey);
        if (StrUtil.isNotBlank(value)) {
            throw new IllegalArgumentException("验证码已生成.");
        }
        String code = captcha.getCode();
        redisUtil.setEx(captchaKey, code, 10, TimeUnit.MINUTES);
        return captcha;
    }

    @Override
    public boolean validCaptcha(RedisCaptcha captcha, String value) {
        return redisUtil.validCodeVal(captcha.getKey(), value, false);
    }

    @Override
    public void clearCaptcha(RedisCaptcha captcha) {
        redisUtil.delete(captcha.getKey());
    }

    @Override
    public boolean validClearCaptcha(RedisCaptcha captcha, String value) {
        return redisUtil.validCodeVal(captcha.getKey(), value, true);
    }
}
