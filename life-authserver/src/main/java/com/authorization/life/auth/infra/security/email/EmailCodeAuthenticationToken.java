package com.authorization.life.auth.infra.security.email;

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
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {

    public static final String EMAIL_CAPTCHA = "email_captcha";
    public static final String CAPTCHA_CODE = "captcha_code";

    public EmailCodeAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
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
