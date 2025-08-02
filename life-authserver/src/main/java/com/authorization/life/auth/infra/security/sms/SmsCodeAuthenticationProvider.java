package com.authorization.life.auth.infra.security.sms;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.authorization.life.auth.infra.security.base.OAuth2BaseAuthenticationProvider;
import com.authorization.life.auth.infra.security.service.RegisteredClientService;
import com.authorization.life.security.start.UserDetailService;
import com.authorization.redis.start.model.RedisCaptcha;
import com.authorization.redis.start.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2025-06-14 22:01
 */
@Slf4j
public class SmsCodeAuthenticationProvider
        extends OAuth2BaseAuthenticationProvider<SmsCodeAuthenticationToken> {

    /**
     * 验证码uuid
     */
    public static final String CAPTCHA_UUID = "captcha_uuid";
    /**
     * 验证码
     */
    public static final String CAPTCHA_CODE = "captcha_code";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken codeAuthenticationToken =
                (SmsCodeAuthenticationToken) authentication;

        AuthorizationGrantType authorizationGrantType =
                codeAuthenticationToken.getAuthorizationGrantType();

        String username = codeAuthenticationToken.getUsername();

        String captchaUuid = codeAuthenticationToken.getCaptchaUuid();

        String captchaCode = codeAuthenticationToken.getCaptchaCode();

        String clientId = codeAuthenticationToken.getClientId();

        String clientSecret = codeAuthenticationToken.getClientSecret();

        Set<String> scopes = codeAuthenticationToken.getScopes();

        Map<String, Object> additionalParameters =
                codeAuthenticationToken.getAdditionalParameters();

        UserDetailService userDetailService = SpringUtil.getBean(UserDetailService.class);
        RegisteredClientService registeredClientService =
                SpringUtil.getBean(RegisteredClientService.class);
        CaptchaService captchaService = SpringUtil.getBean(CaptchaService.class);

        // 验证client是否正确, 授权域是否正确.
        RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
        // 校验是否支持授权模式, 验证clientSecret, 检查 scopes
        registeredClientService.checkClient(
                registeredClient, authorizationGrantType, clientSecret, scopes);
        // 验证用户信息
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // 校验验证码是否正确
        RedisCaptcha genCaptcha = RedisCaptcha.of(RedisCaptcha.CaptchaType.SEND_SMS_CODE_LOGIN, username, captchaUuid);
        boolean validCaptcha = captchaService.validClearCaptcha(genCaptcha, captchaCode);
        Assert.isTrue(validCaptcha, () -> new OAuth2AuthenticationException("验证码不正确."));

        // 验证用户状态
        new AccountStatusUserDetailsChecker().check(userDetails);
        // 创建授权通过的用户认证信息
        UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(
                        userDetails, codeAuthenticationToken.getCredentials(), userDetails.getAuthorities());

        // 创建 accessToken
        OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
                createOAuth2AccessTokenAuthenticationToken(
                        registeredClient,
                        authenticated,
                        scopes,
                        authorizationGrantType,
                        codeAuthenticationToken,
                        additionalParameters
                );
        log.info("SmsCodeAuthenticationProvider:{}", oAuth2AccessTokenAuthenticationToken);
        return oAuth2AccessTokenAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
