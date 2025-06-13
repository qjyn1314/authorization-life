package com.authorization.common.exception.enums;

import com.authorization.common.exception.ErrorMsgFormat;
import com.authorization.utils.security.UserDetail;
import com.authorization.utils.security.UserDetailUtil;

import java.util.Objects;

/**
 * 错误信息编码接口
 *
 * @author wangjunming
 * @date 2023/1/4 14:07
 */
public interface ExceptionErrorCode {

    /**
     * 获取编码
     */
    Integer getCode();

    /**
     * 中文消息
     */
    String getMsg();

    /**
     * 获取消息对应国际化中的key
     */
    String getMsgCode();

    /**
     * 获取Code对应的国际化消息
     */
    default String formatMsg() {
        UserDetail userContext = UserDetailUtil.getUserContext();
        if (Objects.isNull(userContext)) {
            return this.getMsg();
        }
        return ErrorMsgFormat.getMsg(getMsgCode(), userContext.getLocale());
    }


}
