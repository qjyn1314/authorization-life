package com.authserver.common.jwt;

/**
 * jwt通用异常
 */
public class JwtException extends RuntimeException {

    private static final String MODULE = "JWT Exception";

    public JwtException(Throwable cause, Object... args) {
        super(MODULE, cause);
    }
}
