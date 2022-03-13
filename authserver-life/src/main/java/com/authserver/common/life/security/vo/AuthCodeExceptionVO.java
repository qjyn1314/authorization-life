package com.authserver.common.life.security.vo;

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

    private String username;

    private String password;

    private String phone;

    private String captchaCode;

    private String defaultDesc;
}
