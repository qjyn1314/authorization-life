package com.authorization.life.auth.infra.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 使用redis缓存  OAuth2AuthorizationConsent
 */
@RequiredArgsConstructor
public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    /**
     * 登录过程中所需要存储信息的 redisKey前缀
     */
    static String AUTHORIZATION_KET_PREFIX = "sso-oauth-server:auth:token:";

    private final RedisTemplate<String, Object> redisTemplate;

    private final static Long TIMEOUT = 10L;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.opsForValue()
                .set(buildKey(authorizationConsent), authorizationConsent, TIMEOUT, TimeUnit.MINUTES);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.delete(buildKey(authorizationConsent));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue()
                .get(buildKey(registeredClientId, principalName));
    }

    private static String buildKey(String registeredClientId, String principalName) {

        return AUTHORIZATION_KET_PREFIX + "token-consent:" + registeredClientId + ":" + principalName;
    }

    private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
        return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }


}
