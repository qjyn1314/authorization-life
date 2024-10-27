package com.authorization.life.auth.app.constant;

import cn.hutool.core.util.StrUtil;
import com.authorization.core.exception.handle.CommonException;
import com.authorization.redis.start.util.RedisCaptchaValidator;
import com.authorization.redis.start.util.RedisUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-10 09:00
 */
public class RedisKeyValid {

    public static final String EMAIL_SEND_COUNT = "EMAIL_SEND_COUNT:{email}";

    public static String getEmailSendCountKey(String email) {
        return StrUtil.format(EMAIL_SEND_COUNT, Map.of("email", email));
    }

    public static void validEmailRepeatSendCode(RedisUtil redisUtil, String email) {
        String emailSendCountKey = getEmailSendCountKey(email);
        String emailSendValue = redisUtil.get(emailSendCountKey);
        if (StrUtil.isNotBlank(emailSendValue)) {
            throw new CommonException("邮箱已发送验证码请查收.");
        }
        redisUtil.setEx(emailSendCountKey, emailSendCountKey, 10, TimeUnit.MINUTES);
    }

    public static void delEmailRepeatSendCode(RedisUtil redisUtil, String email) {
        redisUtil.delete(getEmailSendCountKey(email));
    }

    public static void delEmailRegSendCode(RedisUtil redisUtil, String uuid) {
        redisUtil.delete(StrUtil.format(RedisCaptchaValidator.CAPTCHA_CACHE_KEY, Map.of("uuid", uuid)));
    }

}
