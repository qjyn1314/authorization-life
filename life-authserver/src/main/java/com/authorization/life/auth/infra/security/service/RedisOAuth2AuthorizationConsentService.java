package com.authorization.life.auth.infra.security.service;

import com.authorization.utils.security.SecurityCoreService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

/**
 * 使用redis缓存  OAuth2AuthorizationConsent
 */
public final class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private static final String AUTHORIZATION = SecurityCoreService.AUTHORIZATION_CONSENT;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisOAuth2AuthorizationConsentService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.opsForHash().put(AUTHORIZATION, getId(authorizationConsent), authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.opsForHash().delete(AUTHORIZATION, getId(authorizationConsent));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        return (OAuth2AuthorizationConsent) redisTemplate.opsForHash().get(AUTHORIZATION, getId(registeredClientId, principalName));
    }

    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }

    private static String getId(String registeredClientId, String principalName) {
        return registeredClientId + principalName;
    }

}
