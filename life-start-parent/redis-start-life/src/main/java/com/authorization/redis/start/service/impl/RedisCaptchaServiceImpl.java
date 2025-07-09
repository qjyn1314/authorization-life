package com.authorization.redis.start.service.impl;

import com.authorization.redis.start.model.RedisCaptcha;
import com.authorization.redis.start.service.RedisCaptchaService;
import org.springframework.stereotype.Service;

/**
 * @author wangjunming
 * @since 2025-07-09 15:21
 */
@Service
public class RedisCaptchaServiceImpl implements RedisCaptchaService {


    @Override
    public RedisCaptcha genNumCaptcha(RedisCaptcha captchaModel) {
        return null;
    }

    @Override
    public RedisCaptcha genCaptcha(RedisCaptcha captchaModel) {
        return null;
    }

    @Override
    public boolean validCaptcha(RedisCaptcha captchaModel) {
        return false;
    }

    @Override
    public void clearCaptcha(RedisCaptcha captchaModel) {

    }
}
