package com.authorization.life.auth.infra.security.sms;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.*;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-14 22:02
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 授权模式-手机验证码模式
     */
    public static final String PHONE_CAPTCHA = "phone_captcha";
    @Getter
    private final AuthorizationGrantType authorizationGrantType;
    @Getter
    private final Authentication clientPrincipal;
    @Getter
    private final String username;
    @Getter
    private final String captchaCode;
    @Getter
    private final String captchaUuid;
    @Getter
    private final String clientId;
    @Getter
    private final String clientSecret;
    @Getter
    private final Set<String> scopes;
    @Getter
    private final Map<String, Object> additionalParameters;

    public SmsCodeAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                      Authentication clientPrincipal, String username, String captchaUuid, String captchaCode,
                                      String clientId, String clientSecret, Set<String> scopes, Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.username = username;
        this.captchaUuid = captchaUuid;
        this.captchaCode = captchaCode;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scopes =
                Collections.unmodifiableSet(
                        scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters =
                Collections.unmodifiableMap(
                        additionalParameters != null
                                ? new HashMap<>(additionalParameters)
                                : Collections.emptyMap());
    }

    @Override
    public Object getCredentials() {
        return Map.of(SmsCodeAuthenticationProvider.CAPTCHA_CODE, captchaCode, SmsCodeAuthenticationProvider.CAPTCHA_UUID, captchaUuid);
    }

    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }
}
