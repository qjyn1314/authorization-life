package com.authorization.redis.start.service;

import com.authorization.redis.start.model.RedisCaptcha;

/**
 * @author wangjunming
 * @since 2025-07-09 15:21
 */
public interface RedisCaptchaService {

  /**
   * 生成6为数字的动态
   * @param captchaModel
   * @return
   */
  RedisCaptcha genNumCaptcha(RedisCaptcha captchaModel);

  RedisCaptcha genCaptcha(RedisCaptcha captchaModel);

  boolean validCaptcha(RedisCaptcha captchaModel);

  void clearCaptcha(RedisCaptcha captchaModel);
}
