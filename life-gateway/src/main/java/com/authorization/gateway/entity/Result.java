package com.authorization.gateway.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * json标准对象
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    public final static String SUCCESS = "0";
    public final static String ERROR = "-1";
    public static final String SUCCESS_DESC = "操作成功";

    private String code;
    private String desc;

}
