package com.authorization.common.exception.enums;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-13 16:23
 */
public enum ExceptionErrorEnums implements ExceptionErrorCode {

    // 10000 ~ 11000 是系统默认的错误消息



    /**
     * 系统异常，请稍后重试。
     */
    SYSTEM_EXCEPTION(10000, "系统异常，请稍后重试。", "life-core.system-exception"),

    /**
     * 系统异常，请稍后重试。
     */
    SYSTEM_EXCEPTION(10000, "系统异常，请稍后重试。", "life-core.system-exception"),
    /**
     * 系统异常, 数据不存在.
     */
    DATA_NOT_EXIST(10001, "系统异常, 数据不存在.", "life-core.data-does-not-exist"),


    ;

    ExceptionErrorEnums(Integer code, String message, String messageCode) {
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
