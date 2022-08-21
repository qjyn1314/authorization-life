package com.authorization.life.security.util;

import cn.hutool.captcha.ShearCaptcha;
import lombok.Getter;
import lombok.Setter;

/**
 * 验证码生成类
 */
@Setter
@Getter
public final class Captcha {

    public static Captcha of(ShearCaptcha shearCaptcha, String uuid) {
        Captcha captcha = new Captcha();
        captcha.uuid = uuid;
        captcha.imageBase64 = shearCaptcha.getImageBase64Data();
        return captcha;
    }

    private String uuid;
    private String imageBase64;
}
