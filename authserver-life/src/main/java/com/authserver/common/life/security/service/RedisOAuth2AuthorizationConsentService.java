//package com.authserver.common.life.security.service;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
//import org.springframework.util.Assert;
//
//public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
//
//    private static final String AUTHORIZATION = "ziam:auth:oauth2:authorization-consent";
//
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public RedisOAuth2AuthorizationConsentService(RedisTemplate<String, String> jdkHashValueRedisTemplate) {
//        this.redisTemplate = jdkHashValueRedisTemplate;
//    }
//
//    @Override
//    public void save(OAuth2AuthorizationConsent authorizationConsent) {
//        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
//        redisTemplate.opsForHash().put(AUTHORIZATION, getId(authorizationConsent), authorizationConsent);
//    }
//
//    @Override
//    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
//        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
//        redisTemplate.opsForHash().delete(AUTHORIZATION, getId(authorizationConsent));
//    }
//
//    @Override
//    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
//        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
//        Assert.hasText(principalName, "principalName cannot be empty");
//        return (OAuth2AuthorizationConsent) redisTemplate.opsForHash().get(AUTHORIZATION, getId(registeredClientId, principalName));
//    }
//
//    private static String getId(String registeredClientId, String principalName) {
//        return registeredClientId + principalName;
//    }
//
//    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
//        return authorizationConsent.getRegisteredClientId() + authorizationConsent.getPrincipalName();
//    }
//}
