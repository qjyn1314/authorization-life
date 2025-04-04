package com.authorization.life.security.start.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * 未登录的异常信息.
 *
 * @author wangjunming
 * @date 2023/3/7 17:30
 */
public class NotLoggedException extends AuthenticationException {

    public NotLoggedException(String message) {
        super(message);
    }
}
