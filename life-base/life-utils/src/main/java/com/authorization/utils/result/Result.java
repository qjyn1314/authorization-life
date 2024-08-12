package com.authorization.utils.result;

import com.authorization.utils.message.MsgFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 公共返回参数信息
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    public static final String FORM_ERROR = "-2";
    public static final String ERROR = "-1";
    public static final String ERROR_MSG = "life-util.operation-failed";
    public static final String SUCCESS = "0";
    public static final String SUCCESS_MSG = "life-util.operation-successful";
    private String code;
    private String msg;
    private T data;
    private transient Object[] args;

    public Result(String code, String msg, Object[] args, T data) {
        this.code = code;
        try {
            this.msg = Objects.nonNull(msg) ? MsgFormat.getMessageLocal(msg, args, null).getMessage() : null;
        } catch (Exception e) {
            this.msg = msg;
        }
        this.args = null;
        this.data = data;
    }

    public static <T> Result<T> okForCodeArgs(String code, String msg, Object[] args, T data) {
        return new Result<>(code, msg, args, data);
    }

    public static <T> Result<T> okForCode(String code, String msg, T data) {
        return okForCodeArgs(code, msg, null, data);
    }

    public static <T> Result<T> okCode(String code, T data) {
        return okForCode(code, null, data);
    }

    public static <T> Result<T> okForArgs(String msg, Object[] args, T data) {
        return okForCodeArgs(SUCCESS, msg, args, data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return okForArgs(msg, null, data);
    }

    public static <T> Result<T> ok(T data) {
        return ok(SUCCESS_MSG, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS, SUCCESS_MSG, null, null);
    }

    public static <T> Result<T> failForCodeArgs(String code, String msg, Object[] args, T data) {
        return new Result<>(code, msg, args, data);
    }

    public static <T> Result<T> failForCode(String code, String msg, T data) {
        return failForCodeArgs(code, msg, null, data);
    }

    public static <T> Result<T> failForCode(String code, String msg, Object[] args) {
        return failForCodeArgs(code, msg, args, null);
    }

    public static <T> Result<T> failCode(String code, String msg) {
        return failForCode(code, msg, null);
    }

    public static <T> Result<T> failForArgs(String msg, Object[] args) {
        return failForCode(ERROR, msg, null);
    }

    public static <T> Result<T> failForArgs(String msg, Object[] args, T data) {
        return failForCodeArgs(ERROR, msg, args, data);
    }

    public static <T> Result<T> failMsg(String msg, T data) {
        return failForCode(ERROR, msg, data);
    }

    public static <T> Result<T> fail(String msg) {
        return failMsg(msg, null);
    }

    public static <T> Result<T> fail(T data) {
        return failMsg(ERROR_MSG, data);
    }

    public static <T> Result<T> fail() {
        return fail(null);
    }

}
