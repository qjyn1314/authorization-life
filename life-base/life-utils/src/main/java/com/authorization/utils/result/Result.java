package com.authorization.utils.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 公共返回参数信息
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    public static final Integer FORM_ERROR = -2;
    public static final Integer ERROR = -1;
    public static final Integer SUCCESS = 0;
    private Integer code;
    private String msg;
    private T data;
    private transient Object[] args;

    private Result(Integer code, String msg, Object[] args, T data) {
        this.code = code;
        this.msg = msg;
        this.args = args;
        this.data = data;
    }

    public static <T> Result<T> okForCodeArgs(Integer code, String msg, Object[] args, T data) {
        return new Result<>(code, msg, args, data);
    }

    public static <T> Result<T> okForCode(Integer code, String msg, T data) {
        return okForCodeArgs(code, msg, null, data);
    }

    public static <T> Result<T> okCode(Integer code, T data) {
        return okForCode(code, null, data);
    }

    public static <T> Result<T> okForArgs(String msg, Object[] args, T data) {
        return okForCodeArgs(SUCCESS, msg, args, data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return okForArgs(msg, null, data);
    }

    public static <T> Result<T> ok(T data) {
        return ok(null, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS, null, null, null);
    }

    public static <T> Result<T> failForCodeArgs(Integer code, String msg, Object[] args, T data) {
        return new Result<>(code, msg, args, data);
    }

    public static <T> Result<T> failForCode(Integer code, String msg, T data) {
        return failForCodeArgs(code, msg, null, data);
    }

    public static <T> Result<T> failForCode(Integer code, String msg, Object[] args) {
        return failForCodeArgs(code, msg, args, null);
    }

    public static <T> Result<T> failCode(Integer code, String msg) {
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
        return failMsg(null, data);
    }

    public static <T> Result<T> fail() {
        return fail(null);
    }

}
