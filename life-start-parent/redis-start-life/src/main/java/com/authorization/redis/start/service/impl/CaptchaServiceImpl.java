package com.authorization.redis.start.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.authorization.redis.start.model.RedisCaptcha;
import com.authorization.redis.start.service.CaptchaService;
import com.authorization.redis.start.util.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wangjunming
 * @since 2025-07-09 15:21
 */
@Slf4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public RedisCaptcha genNumCaptcha(RedisCaptcha captcha) {
        log.info("genNumCaptcha->{}", JSON.toJSONString(captcha));
        String captchaKey = captcha.getKey();
        String value = redisUtil.get(captchaKey);
        if (StrUtil.isNotBlank(value)) {
            throw new IllegalArgumentException("验证码已生成.");
        }
        String code = captcha.getCode();
        redisUtil.setEx(captchaKey, code, captcha.getExpiredTime(), TimeUnit.MINUTES);
        return captcha;
    }

    @Override
    public boolean validCaptcha(RedisCaptcha captcha, String value) {
        log.info("validCaptcha->{}", JSON.toJSONString(captcha));
        return redisUtil.validCodeVal(captcha.getKey(), value, false);
    }

    @Override
    public void clearCaptcha(RedisCaptcha captcha) {
        log.info("clearCaptcha->{}", JSON.toJSONString(captcha));
        redisUtil.delete(captcha.getKey());
    }

    @Override
    public boolean validClearCaptcha(RedisCaptcha captcha, String value) {
        log.info("validClearCaptcha->{}", JSON.toJSONString(captcha));
        return redisUtil.validCodeVal(captcha.getKey(), value, true);
    }
}
