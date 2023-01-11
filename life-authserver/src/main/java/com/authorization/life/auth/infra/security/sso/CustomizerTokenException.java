package com.authorization.life.auth.infra.security.sso;

import org.springframework.security.authentication.AuthenticationServiceException;

public class CustomizerTokenException extends AuthenticationServiceException {
    public CustomizerTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomizerTokenException(String message) {
        super(message);
    }
}
