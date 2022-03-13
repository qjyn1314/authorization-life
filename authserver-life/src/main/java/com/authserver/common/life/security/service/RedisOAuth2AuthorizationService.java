//package com.authserver.common.life.security.service;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.lang.Nullable;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
//import org.springframework.security.oauth2.core.OAuth2RefreshToken;
//import org.springframework.security.oauth2.core.OAuth2TokenType;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.util.Assert;
//
//public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
//
//    private static final String AUTHORIZATION = "ziam:auth:oauth2:authorization";
//
//    private final RedisTemplate<String, String> redisTemplate;
//
//    public RedisOAuth2AuthorizationService(RedisTemplate<String, String> jdkHashValueRedisTemplate) {
//        this.redisTemplate = jdkHashValueRedisTemplate;
//    }
//
//    @Override
//    public void save(OAuth2Authorization authorization) {
//        Assert.notNull(authorization, "authorization cannot be null");
//        redisTemplate.opsForHash().put(AUTHORIZATION, authorization.getId(), authorization);
//
//    }
//
//    @Override
//    public void remove(OAuth2Authorization authorization) {
//        Assert.notNull(authorization, "authorization cannot be null");
//        redisTemplate.opsForHash().delete(AUTHORIZATION, authorization.getId());
//    }
//
//    @Override
//    public OAuth2Authorization findById(String id) {
//        Assert.hasText(id, "id cannot be empty");
//        return (OAuth2Authorization)redisTemplate.opsForHash().get(AUTHORIZATION, id);
//    }
//
//    @Override
//    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
//        Assert.hasText(token, "token cannot be empty");
//        for (Object authorization : redisTemplate.opsForHash().values(AUTHORIZATION)) {
//            if (hasToken((OAuth2Authorization)authorization, token, tokenType)) {
//                return (OAuth2Authorization)authorization;
//            }
//        }
//        return null;
//    }
//
//    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
//        if (tokenType == null) {
//            return matchesState(authorization, token) ||
//                    matchesAuthorizationCode(authorization, token) ||
//                    matchesAccessToken(authorization, token) ||
//                    matchesRefreshToken(authorization, token);
//        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
//            return matchesState(authorization, token);
//        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
//            return matchesAuthorizationCode(authorization, token);
//        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
//            return matchesAccessToken(authorization, token);
//        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
//            return matchesRefreshToken(authorization, token);
//        }
//        return false;
//    }
//
//    private static boolean matchesState(OAuth2Authorization authorization, String token) {
//        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
//    }
//
//    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
//        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
//                authorization.getToken(OAuth2AuthorizationCode.class);
//        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
//    }
//
//    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
//        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
//                authorization.getToken(OAuth2AccessToken.class);
//        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
//    }
//
//    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
//        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
//                authorization.getToken(OAuth2RefreshToken.class);
//        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
//    }
//}
