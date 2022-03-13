package com.authserver.common.life.security.sso;

import org.springframework.security.authentication.AccountStatusException;

public class ValiVerificationCodeException extends AccountStatusException {

    public ValiVerificationCodeException(String msg) {
        super(msg);
    }
    public ValiVerificationCodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
