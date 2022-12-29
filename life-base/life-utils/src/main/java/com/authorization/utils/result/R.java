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
public class R<T> implements Serializable {

    public static final String FORM_ERROR = "-2";
    public static final String ERROR = "-1";
    public static final String ERROR_DESC = "操作失败";
    public static final String SUCCESS = "0";
    public static final String SUCCESS_DESC = "操作成功";
    private String code;
    private String desc;
    private T data;


    public R(String code, String desc, T data) {
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public static <T> R<T> ok(String desc, T data) {
        return new R<>(SUCCESS, desc, data);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(SUCCESS, SUCCESS_DESC, data);
    }

    public static <T> R<T> ok() {
        return new R<>(SUCCESS, SUCCESS_DESC, null);
    }

    public static <T> R<T> fail(String desc, T data) {
        return new R<>(ERROR, desc, data);
    }

    public static <T> R<T> fail(T data) {
        return new R<>(ERROR, ERROR_DESC, data);
    }

    public static <T> R<T> failMsg(String desc) {
        return new R<>(ERROR, desc, null);
    }

}
