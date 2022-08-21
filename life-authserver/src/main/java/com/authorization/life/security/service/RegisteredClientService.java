package com.authorization.life.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.authorization.common.exception.CommonException;
import com.authorization.life.entity.OauthClient;
import com.authorization.life.service.OauthClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义的client信息，查询后进行转化
 */
public class RegisteredClientService implements RegisteredClientRepository {

    private final OauthClientService clientService;

    public RegisteredClientService(OauthClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new CommonException("此处不允许进行保存数据");
    }

    @Override
    public RegisteredClient findById(String clientId) {
        OauthClient oauthClient = clientService.selectClientByClientId(clientId);
        if (Objects.isNull(oauthClient)) {
            return null;
        }
        return getRegisteredClient(clientId, oauthClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OauthClient oauthClient = clientService.selectClientByClientId(clientId);
        if (Objects.isNull(oauthClient)) {
            return null;
        }
        return getRegisteredClient(clientId, oauthClient);
    }

    private RegisteredClient getRegisteredClient(String clientId, OauthClient oauthClient) {
        RegisteredClient.Builder builder = RegisteredClient.withId(clientId)
                .clientId(oauthClient.getClientId())
                .clientSecret(oauthClient.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .redirectUri(oauthClient.getRedirectUri())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.of(oauthClient.getAccessTokenTimeout(), ChronoUnit.SECONDS))
                        .refreshTokenTimeToLive(Duration.of(oauthClient.getRefreshTokenTimeout(), ChronoUnit.SECONDS))
                        .build());
        //批量设置当前的授权类型
        Arrays.stream(oauthClient.getGrantTypes().split(StrPool.COMMA))
                .map(grantType -> {
                    if (CharSequenceUtil.equals(grantType, AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
                        return AuthorizationGrantType.AUTHORIZATION_CODE;
                    } else if (CharSequenceUtil.equals(grantType, AuthorizationGrantType.REFRESH_TOKEN.getValue())) {
                        return AuthorizationGrantType.REFRESH_TOKEN;
                    } else if (CharSequenceUtil.equals(grantType, AuthorizationGrantType.CLIENT_CREDENTIALS.getValue())) {
                        return AuthorizationGrantType.CLIENT_CREDENTIALS;
                    } else if (CharSequenceUtil.equals(grantType, AuthorizationGrantType.PASSWORD.getValue())) {
                        return AuthorizationGrantType.PASSWORD;
                    } else if (CharSequenceUtil.equals(grantType, AuthorizationGrantType.JWT_BEARER.getValue())) {
                        return AuthorizationGrantType.JWT_BEARER;
                    } else {
                        throw new CommonException("不支持的授权模式, [" + grantType + "]");
                    }
                }).forEach(builder::authorizationGrantType);
        Arrays.stream(oauthClient.getScopes().split(StrPool.COMMA))
                .forEach(builder::scope);
        return builder.build();
    }

}
