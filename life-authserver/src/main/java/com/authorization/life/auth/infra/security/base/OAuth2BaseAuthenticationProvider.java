package com.authorization.life.auth.infra.security.base;

import cn.hutool.extra.spring.SpringUtil;
import com.authorization.life.auth.infra.security.service.RegisteredClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

import java.security.Principal;
import java.util.Map;
import java.util.Set;

/**
 * @author wangjunming
 * @since 2025-07-30 16:31
 */
@Slf4j
public abstract class OAuth2BaseAuthenticationProvider<T extends AbstractAuthenticationToken>
        implements AuthenticationProvider {

    private static final String ERROR_URI =
            "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    /**
     * 创建token
     *
     * @param registeredClient 客户端信息
     * @param authenticated 认证通过后的用户信息
     * @param scopes 授权域
     * @param authorizationGrantType 授权类型
     * @param authenticationToken 当前使用的convert转换后的token
     * @param additionalParameters 附加信息
     * @return OAuth2AccessTokenAuthenticationToken
     */
    public OAuth2AccessTokenAuthenticationToken createOAuth2AccessTokenAuthenticationToken(
            RegisteredClient registeredClient,
            UsernamePasswordAuthenticationToken authenticated,
            Set<String> scopes,
            AuthorizationGrantType authorizationGrantType,
            Authentication authenticationToken,
            Map<String, Object> additionalParameters) {

        DelegatingOAuth2TokenGenerator oAuth2TokenGenerator =
                SpringUtil.getBean(DelegatingOAuth2TokenGenerator.class);
        RegisteredClientService registeredClientService =
                SpringUtil.getBean(RegisteredClientService.class);
        OAuth2AuthorizationService oAuth2AuthorizationService =
                SpringUtil.getBean(OAuth2AuthorizationService.class);

        DefaultOAuth2TokenContext.Builder tokenContextBuilder =
                DefaultOAuth2TokenContext.builder()
                        .registeredClient(registeredClient)
                        .principal(authenticated)
                        .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                        .authorizedScopes(scopes)
                        .authorizationGrantType(authorizationGrantType)
                        .authorizationGrant(authenticationToken);

        // OAuth2RefreshTokenAuthenticationProvider 中有生成示例

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

        // 校验是否支持 生成 REFRESH_TOKEN
        registeredClientService.checkClient(
                registeredClient, AuthorizationGrantType.REFRESH_TOKEN, null, null);

        // ----- Refresh token -----
        tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
        OAuth2Token generatedRefreshToken = oAuth2TokenGenerator.generate(tokenContext);
        if (!(generatedRefreshToken instanceof OAuth2RefreshToken refreshToken)) {
            OAuth2Error error =
                    new OAuth2Error(
                            OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate the refresh token.",
                            ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }


        OAuth2Authorization.Builder authorizationBuilder =
                OAuth2Authorization.withRegisteredClient(registeredClient)
                        .principalName(authenticationToken.getName())
                        .authorizationGrantType(authorizationGrantType)
                        .authorizedScopes(scopes)
                        .id(accessToken.getTokenValue())
                        .token(
                                accessToken,
                                (metadata) ->
                                        metadata.put(
                                                OAuth2Authorization.Token.CLAIMS_METADATA_NAME,
                                                ((ClaimAccessor) generatedAccessToken).getClaims()))
                        .authorizedScopes(scopes)
                        .attribute(Principal.class.getName(), authenticationToken);

        authorizationBuilder.refreshToken(refreshToken);

        OAuth2Authorization authorization = authorizationBuilder.build();

        oAuth2AuthorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, authenticationToken, accessToken, refreshToken, additionalParameters);
    }
}
