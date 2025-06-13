package com.authorization.common.exception.enums;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-13 16:23
 */
public enum SecurityErrorEnums implements ExceptionErrorCode {

    // 11000 ~ 12000 是系统默认的错误消息
    /**
     * 登录失效，请重新登录。
     */
    LOGIN_IS_INVALID(10001, "登录失效，请重新登录。", "life-core.token-of-header-not-found"),
    /**
     * 当前登录过期，请重新登录。
     */
    USER_LOGIN_EXPIRED(10001, "当前登录过期，请重新登录。", "life-core.user-login-expired"),

    ;

    SecurityErrorEnums(Integer code, String message, String messageCode) {
        this.code = code;
        this.message = message;
        this.messageCode = messageCode;
    }

    private final Integer code;
    private final String message;
    private final String messageCode;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }

    @Override
    public String getMsgCode() {
        return this.messageCode;
    }
}
