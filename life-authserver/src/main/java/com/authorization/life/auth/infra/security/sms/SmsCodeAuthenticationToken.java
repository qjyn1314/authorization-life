package com.authorization.life.auth.infra.security.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    public static final String PHONE_CAPTCHA = "phone_captcha";

    public SmsCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
