package com.authorization.life.auth.infra.security.password;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.authorization.life.auth.infra.security.base.OAuth2BaseAuthenticationProvider;
import com.authorization.life.auth.infra.security.service.RegisteredClientService;
import com.authorization.life.security.start.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Map;
import java.util.Set;

/**
 * @author wangjunming
 * @since 2025-06-14 22:01
 */
@Slf4j
public class PasswordAuthenticationProvider
        extends OAuth2BaseAuthenticationProvider<PasswordAuthenticationToken> {

    public PasswordAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        PasswordAuthenticationToken passwordAuthenticationToken =
                (PasswordAuthenticationToken) authentication;

        AuthorizationGrantType authorizationGrantType =
                passwordAuthenticationToken.getAuthorizationGrantType();

        String username = passwordAuthenticationToken.getUsername();

        String password = passwordAuthenticationToken.getPassword();

        String clientId = passwordAuthenticationToken.getClientId();

        String clientSecret = passwordAuthenticationToken.getClientSecret();

        Set<String> scopes = passwordAuthenticationToken.getScopes();

        Map<String, Object> additionalParameters =
                passwordAuthenticationToken.getAdditionalParameters();

        UserDetailService userDetailService = SpringUtil.getBean(UserDetailService.class);
        PasswordEncoder passwordEncoder = SpringUtil.getBean(PasswordEncoder.class);
        RegisteredClientService registeredClientService =
                SpringUtil.getBean(RegisteredClientService.class);

        // 验证client是否正确, 授权域是否正确.
        RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
        // 校验是否支持password模式, 验证clientSecret, 检查 scopes
        registeredClientService.checkClient(
                registeredClient, authorizationGrantType, clientSecret, scopes);
        // 验证用户信息
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        // 验证密码是否正确
        String detailsPassword = userDetails.getPassword();
        boolean matches = passwordEncoder.matches(password, detailsPassword);
        Assert.isTrue(matches, () -> new BadCredentialsException("用户名或密码错误."));
        // 验证用户状态
        new AccountStatusUserDetailsChecker().check(userDetails);
        // 创建授权通过的用户认证信息
        UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(
                        userDetails, password, userDetails.getAuthorities());

        // 创建 accessToken
        OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
                createOAuth2AccessTokenAuthenticationToken(
                        registeredClient,
                        authenticated,
                        scopes,
                        authorizationGrantType,
                        passwordAuthenticationToken,
                        additionalParameters
                );
        log.info("PasswordAuthenticationProvider:{}", oAuth2AccessTokenAuthenticationToken);
        return oAuth2AccessTokenAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = PasswordAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }
}
