package com.authserver.common.life.security.service;

import cn.hutool.core.util.StrUtil;
import com.authserver.common.life.entity.OauthClient;
import com.authserver.common.life.exception.CommonException;
import com.authserver.common.life.service.OauthClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
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
                        .accessTokenTimeToLive(Duration.of(oauthClient.getAccessTokenTimeout(), ChronoUnit.SECONDS))
                        .refreshTokenTimeToLive(Duration.of(oauthClient.getRefreshTokenTimeout(), ChronoUnit.SECONDS))
                        .build());
        Arrays.stream(oauthClient.getGrantTypes().split(","))
                .map(grantType -> {
                    if (StrUtil.equals(grantType, "authorization_code")) {
                        return AuthorizationGrantType.AUTHORIZATION_CODE;
                    } else if (StrUtil.equals(grantType, "refresh_token")) {
                        return AuthorizationGrantType.REFRESH_TOKEN;
                    } else if (StrUtil.equals(grantType, "client_credentials")) {
                        return AuthorizationGrantType.CLIENT_CREDENTIALS;
                    } else if (StrUtil.equals(grantType, "password")) {
                        return AuthorizationGrantType.PASSWORD;
                    } else if (StrUtil.equals(grantType, "jwt-bearer")) {
                        return AuthorizationGrantType.JWT_BEARER;
                    } else if (StrUtil.equals(grantType, "implicit")) {
                        return AuthorizationGrantType.IMPLICIT;
                    } else {
                        throw new CommonException("不支持的授权模式, [" + grantType + "]");
                    }
                }).forEach(builder::authorizationGrantType);
        Arrays.stream(oauthClient.getScopes().split(",")).forEach(builder::scope);
        return builder.build();
    }

}
