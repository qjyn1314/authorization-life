package com.authorization.life.security.sso;

import org.springframework.security.authentication.AuthenticationServiceException;

public class RegClientException extends AuthenticationServiceException {
    public RegClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegClientException(String message) {
        super(message);
    }
}
