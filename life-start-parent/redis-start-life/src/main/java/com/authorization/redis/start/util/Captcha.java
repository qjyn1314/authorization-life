package com.authorization.redis.start.util;

import cn.hutool.captcha.GifCaptcha;
import lombok.Getter;
import lombok.Setter;

/**
 * 验证码生成类
 */
@Setter
@Getter
public final class Captcha {

    public static Captcha of(GifCaptcha shearCaptcha, String uuid) {
        Captcha captcha = new Captcha();
        captcha.uuid = uuid;
        captcha.imageBase64 = shearCaptcha.getImageBase64Data();
        captcha.code = shearCaptcha.getCode();
        return captcha;
    }

    private String uuid;
    private String imageBase64;
    private String code;
}
