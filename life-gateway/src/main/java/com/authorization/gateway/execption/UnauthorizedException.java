package com.authorization.gateway.execption;

import org.springframework.http.HttpStatus;

/**
 * jwtToken 认证失败
 */
public class UnauthorizedException extends RuntimeException {

    private static final String MODULE = "Unauthorized Exception";

    private static final String DEFAULT_MESSAGE = "The User not login or login status was expired";

    public UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
