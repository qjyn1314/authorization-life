package com.authorization.life.auth.infra.security.sso;

import org.springframework.security.authentication.AuthenticationServiceException;

public class RegClientException extends AuthenticationServiceException {
    public RegClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegClientException(String message) {
        super(message);
    }
}
