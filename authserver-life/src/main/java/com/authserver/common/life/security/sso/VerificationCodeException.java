package com.authserver.common.life.security.sso;

import org.springframework.security.authentication.AccountStatusException;

public class VerificationCodeException extends AccountStatusException {

    public VerificationCodeException(String msg) {
        super(msg);
    }
    public VerificationCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
