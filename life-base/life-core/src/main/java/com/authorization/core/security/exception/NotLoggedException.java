package com.authorization.core.security.exception;

import com.authorization.core.exception.handle.CommonException;

/**
 * 未登录的异常信息.
 *
 * @author wangjunming
 * @date 2023/3/7 17:30
 */
public class NotLoggedException extends CommonException {

    public NotLoggedException(String message) {
        super(message);
    }
}
