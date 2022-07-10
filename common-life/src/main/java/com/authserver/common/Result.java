package com.authserver.common;

import lombok.Data;

/**
 * 公共返回参数信息
 */
@Data
public class Result<T> {

    public static final String AUTH_ERROR = "403";
    public static final String AUTH_ERROR_DESC = "未登录，没有权限访问";
    public static final String ERROR = "500";
    public static final String ERROR_DESC = "操作失败";
    public static final String SUCCESS = "200";
    public static final String SUCCESS_DESC = "操作成功";
    private String code;
    private String desc;
    private T data;


    public Result(String code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public static <T> Result<T> ok(String desc, T data) {
        return new Result<>(SUCCESS, desc, data);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS, SUCCESS_DESC, data);
    }

    public static <T> Result<T> fail(String desc, T data) {
        return new Result<>(ERROR, desc, data);
    }

    public static <T> Result<T> fail(T data) {
        return new Result<>(ERROR, ERROR_DESC, data);
    }

    public static <T> Result<T> authFail(String desc, T data) {
        return new Result<>(AUTH_ERROR, desc, data);
    }

    public static <T> Result<T> authFail(T data) {
        return new Result<>(AUTH_ERROR, ERROR_DESC, data);
    }


}
