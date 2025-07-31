package com.authorization.life.auth.infra.security.password;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.authorization.life.auth.infra.security.base.OAuth2BaseAuthenticationProvider;
import com.authorization.life.auth.infra.security.service.RegisteredClientService;
import com.authorization.life.security.start.UserDetailService;
import com.authorization.redis.start.util.RedisUtil;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;

/**
 * @author wangjunming
 * @since 2025-06-14 22:01
 */
@Slf4j
public class PasswordAuthenticationProvider
    extends OAuth2BaseAuthenticationProvider<PasswordAuthenticationToken> {

  private static final String ERROR_URI =
      "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

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
    RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
    OAuth2AuthorizationService oAuth2AuthorizationService =
        SpringUtil.getBean(OAuth2AuthorizationService.class);
    DelegatingOAuth2TokenGenerator oAuth2TokenGenerator =
        SpringUtil.getBean(DelegatingOAuth2TokenGenerator.class);

    // 验证client是否正确, 授权域是否正确.
    RegisteredClient registeredClient = registeredClientService.findByClientId(clientId);
    registeredClientService.checkClient(
        registeredClient, authorizationGrantType, clientSecret, scopes);
    // 验证用户信息
    UserDetails userDetails = userDetailService.loadUserByUsername(username);
    // 验证密码是否正确
    String detailsPassword = userDetails.getPassword();
    boolean matches = passwordEncoder.matches(password, detailsPassword);
    Assert.isFalse(matches, () -> new BadCredentialsException("用户名或密码错误."));
    // 验证用户状态
    new AccountStatusUserDetailsChecker().check(userDetails);
    // 创建授权通过的用户认证信息
    UsernamePasswordAuthenticationToken authenticated =
        UsernamePasswordAuthenticationToken.authenticated(
            userDetails, password, userDetails.getAuthorities());

    // 生成accessToken
    // @formatter:off
    DefaultOAuth2TokenContext.Builder tokenContextBuilder =
        DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(authenticated)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizedScopes(scopes)
            .authorizationGrantType(authorizationGrantType)
            .authorizationGrant(passwordAuthenticationToken);
    // @formatter:on

    OAuth2Authorization.Builder authorizationBuilder =
        OAuth2Authorization.withRegisteredClient(registeredClient)
            .principalName(passwordAuthenticationToken.getName())
            .authorizationGrantType(authorizationGrantType)
            // 0.4.0 新增的方法
            .authorizedScopes(scopes);

    // ----- Access token -----
    OAuth2TokenContext tokenContext =
        tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
    OAuth2Token generatedAccessToken = oAuth2TokenGenerator.generate(tokenContext);
    if (generatedAccessToken == null) {
      OAuth2Error error =
          new OAuth2Error(
              OAuth2ErrorCodes.SERVER_ERROR,
              "The token generator failed to generate the access token.",
              ERROR_URI);
      throw new OAuth2AuthenticationException(error);
    }
    OAuth2AccessToken accessToken =
        new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(),
            generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(),
            tokenContext.getAuthorizedScopes());
    authorizationBuilder
        .id(accessToken.getTokenValue())
        .token(
            accessToken,
            (metadata) ->
                metadata.put(
                    OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                    ((ClaimAccessor) generatedAccessToken).getClaims()))
        // 0.4.0 新增的方法
        .authorizedScopes(scopes)
        .attribute(Principal.class.getName(), passwordAuthenticationToken);

    // 生成refrenToken
    registeredClientService.checkClient(
        registeredClient, AuthorizationGrantType.REFRESH_TOKEN, null, null);
    // ----- Refresh token -----
    OAuth2RefreshToken refreshToken = null;

    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
    OAuth2Token generatedRefreshToken = oAuth2TokenGenerator.generate(tokenContext);
    if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
      OAuth2Error error =
          new OAuth2Error(
              OAuth2ErrorCodes.SERVER_ERROR,
              "The token generator failed to generate the refresh token.",
              ERROR_URI);
      throw new OAuth2AuthenticationException(error);
    }
    refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
    authorizationBuilder.refreshToken(refreshToken);

    OAuth2Authorization authorization = authorizationBuilder.build();

    oAuth2AuthorizationService.save(authorization);

    log.debug("returning OAuth2AccessTokenAuthenticationToken");

    // 构建 Oauth2AccessToken 并返回

    log.info("PasswordAuthenticationProvider:{}", authentication);
    log.info("PasswordAuthenticationProvider:{}", passwordAuthenticationToken);
    log.info("PasswordAuthenticationProvider:{}", passwordAuthenticationToken);

    OAuth2AccessTokenAuthenticationToken oAuth2AccessTokenAuthenticationToken =
        new OAuth2AccessTokenAuthenticationToken(
            registeredClient,
            passwordAuthenticationToken,
            accessToken,
            refreshToken,
            additionalParameters);
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
