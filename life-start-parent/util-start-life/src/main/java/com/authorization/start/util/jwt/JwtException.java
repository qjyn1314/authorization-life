package com.authorization.start.util.jwt;

/**
 * jwt通用异常
 */
public class JwtException extends RuntimeException {

    private static final String MODULE = "JWT Exception";

    public JwtException(Throwable cause, Object... args) {
        super(MODULE, cause);
    }
}
