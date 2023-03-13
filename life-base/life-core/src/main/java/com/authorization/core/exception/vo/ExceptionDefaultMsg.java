package com.authorization.core.exception.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 错误消息编码,对应的错误消息内容
 *
 * @author wangjunming
 * @date 2023/3/7 15:16
 */
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDefaultMsg implements ExceptionErrorCode {

    private String code;
    private String msgCode;

    /**
     * 仅有提示消息
     *
     * @param msgCode 错误提示编码
     * @return ExceptionMsg
     */
    public static ExceptionDefaultMsg of(String msgCode) {
        return new ExceptionDefaultMsg().setMsgCode(msgCode);
    }

    /**
     * 提示编码 && 提示消息
     *
     * @param code    返回给前端的提示编码
     * @param msgCode 错误提示编码
     * @return ExceptionMsg
     */
    public static ExceptionDefaultMsg of(String code, String msgCode) {
        return new ExceptionDefaultMsg().setCode(code).setMsgCode(msgCode);
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsgCode() {
        return this.msgCode;
    }
}
