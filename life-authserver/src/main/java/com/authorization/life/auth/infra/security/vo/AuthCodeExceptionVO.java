package com.authorization.life.auth.infra.security.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录异常数据封装
 * </p>
 */
@Data
@Accessors(chain = true)
public class AuthCodeExceptionVO {

    private ErrorVO errorVO;

    private Boolean showCaptchaCode;

    @Data
    @Accessors(chain = true)
    public static class ErrorVO {

        private String username;

        private String password;

        private String phone;

        private String captchaCode;

        private String defaultDesc;
    }

}
