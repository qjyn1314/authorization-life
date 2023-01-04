package com.authorization.life.auth.infra.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.json.JSONUtil;
import com.authorization.utils.exception.CommonException;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.entity.OauthClient;
import com.authorization.life.auth.infra.security.sso.RegClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义的client信息，查询后进行转化
 */
@Slf4j
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
        log.debug("findById-{}", JSONUtil.toJsonStr(oauthClient));
        return getRegisteredClient(clientId, oauthClient);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        OauthClient oauthClient = clientService.selectClientByClientId(clientId);
        if (Objects.isNull(oauthClient)) {
            return null;
        }
        log.debug("findByClientId-{}", JSONUtil.toJsonStr(oauthClient));
        return getRegisteredClient(clientId, oauthClient);
    }

    /**
     * 根据数据库中的client信息转换
     *
     * @param clientId    clientId
     * @param oauthClient 数据库client
     * @return RegisteredClient
     */
    private RegisteredClient getRegisteredClient(String clientId, OauthClient oauthClient) {
        RegisteredClient.Builder builder = RegisteredClient.withId(clientId)
                .clientId(oauthClient.getClientId())
                .clientSecret(oauthClient.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
                .clientAuthenticationMethod(ClientAuthenticationMethod.PRIVATE_KEY_JWT)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .redirectUri(oauthClient.getRedirectUri())
                // JWT的配置项 包括TTL  是否复用refreshToken等等
                .clientSettings(ClientSettings.builder()
                        //是否需要用户确认一下客户端需要获取用户的哪些权限
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        //配置使用自定义的jwtToken格式化，配置此处才会使用到 CustomizerOAuth2Token ， 或者不配置此格式化的配置，将默认生成jwt的形式
                        .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                        //是否可重用刷新令牌
                        .reuseRefreshTokens(true)
                        //accessToken 的有效期  单位：秒
                        .accessTokenTimeToLive(Duration.of(oauthClient.getAccessTokenTimeout(), ChronoUnit.SECONDS))
                        //refreshToken 的有效期   单位：秒
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
                    } else {
                        throw new RegClientException("不支持的授权模式, [" + grantType + "]");
                    }
                }).forEach(builder::authorizationGrantType);
        Arrays.stream(oauthClient.getScopes().split(StrPool.COMMA))
                .forEach(builder::scope);
        return builder.build();
    }

}
