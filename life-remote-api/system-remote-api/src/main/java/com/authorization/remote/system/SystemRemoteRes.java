package com.authorization.remote.system;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * dubbo接口结果封装类
 *
 * @author wangjunming
 * @date 2023/4/17 15:12
 */
@Setter
@Getter
public class SystemRemoteRes<T> implements Serializable {

    public static final String ERROR = "-1";
    public static final String SUCCESS = "0";
    public static final String ERROR_MSG = "rpc-fail";
    public static final String SUCCESS_MSG = "rpc-successful";

    private String code;
    private String msg;
    private T data;
    private transient Object[] args;

    public SystemRemoteRes(String code, String msg, Object[] args, T data) {
        this.code = code;
        this.msg = msg;
        this.args = null;
        this.data = data;
    }

    public static <T> SystemRemoteRes<T> okForCodeArgs(String code, String msg, Object[] args, T data) {
        return new SystemRemoteRes<>(code, msg, args, data);
    }

    public static <T> SystemRemoteRes<T> okForCode(String code, String msg, T data) {
        return okForCodeArgs(code, msg, null, data);
    }

    public static <T> SystemRemoteRes<T> okCode(String code, T data) {
        return okForCode(code, null, data);
    }

    public static <T> SystemRemoteRes<T> okForArgs(String msg, Object[] args, T data) {
        return okForCodeArgs(SUCCESS, msg, args, data);
    }

    public static <T> SystemRemoteRes<T> ok(String msg, T data) {
        return okForArgs(msg, null, data);
    }

    public static <T> SystemRemoteRes<T> ok(T data) {
        return ok(SUCCESS_MSG, data);
    }

    public static <T> SystemRemoteRes<T> ok() {
        return new SystemRemoteRes<>(SUCCESS, SUCCESS_MSG, null, null);
    }

    public static <T> SystemRemoteRes<T> failForCodeArgs(String code, String msg, Object[] args, T data) {
        return new SystemRemoteRes<>(code, msg, args, data);
    }

    public static <T> SystemRemoteRes<T> failForCode(String code, String msg, T data) {
        return failForCodeArgs(code, msg, null, data);
    }

    public static <T> SystemRemoteRes<T> failForCode(String code, String msg, Object[] args) {
        return failForCodeArgs(code, msg, args, null);
    }

    public static <T> SystemRemoteRes<T> failCode(String code, String msg) {
        return failForCode(code, msg, null);
    }

    public static <T> SystemRemoteRes<T> failForArgs(String msg, Object[] args) {
        return failForCode(ERROR, msg, null);
    }

    public static <T> SystemRemoteRes<T> failForArgs(String msg, Object[] args, T data) {
        return failForCodeArgs(ERROR, msg, args, data);
    }

    public static <T> SystemRemoteRes<T> failMsg(String msg, T data) {
        return failForCode(ERROR, msg, data);
    }

    public static <T> SystemRemoteRes<T> fail(String msg) {
        return failMsg(msg, null);
    }

    public static <T> SystemRemoteRes<T> fail(T data) {
        return failMsg(ERROR_MSG, data);
    }

    public static <T> SystemRemoteRes<T> fail() {
        return fail(null);
    }


}