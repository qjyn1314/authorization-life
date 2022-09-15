package com.authorization.life.security.service;

import com.authorization.core.security.SecurityConstant;
import com.authorization.redis.start.util.StrRedisHelper;
import com.authorization.start.util.json.JsonHelper;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 使用redis进行缓存  OAuth2Authorization
 */
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String AUTHORIZATION = SecurityConstant.AUTHORIZATION;

    private final StrRedisHelper strRedisHelper;

    public RedisOAuth2AuthorizationService(StrRedisHelper strRedisHelper) {
        this.strRedisHelper = strRedisHelper;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String authorizationStr = JsonHelper.writeValueAsString(authorization);
        strRedisHelper.hashPut(AUTHORIZATION, authorization.getId(), authorizationStr);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        strRedisHelper.hashDelete(AUTHORIZATION, authorization.getId());
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return JsonHelper.readValue(strRedisHelper.hashGet(AUTHORIZATION, id),OAuth2Authorization.class);
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
//        for (Object authorization : redisTemplate.opsForHash().values(AUTHORIZATION)) {
//            if (hasToken((OAuth2Authorization) authorization, token, tokenType)) {
//                return (OAuth2Authorization) authorization;
//            }
//        }
        return null;
    }

    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
        if (tokenType != null) {
            if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
                return matchesState(authorization, token);
            } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
                return matchesAuthorizationCode(authorization, token);
            } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
                return matchesAccessToken(authorization, token);
            } else {
                return OAuth2TokenType.REFRESH_TOKEN.equals(tokenType) && matchesRefreshToken(authorization, token);
            }
        }
        return matchesState(authorization, token)
                || matchesAuthorizationCode(authorization, token)
                || matchesAccessToken(authorization, token)
                || matchesRefreshToken(authorization, token);

    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }
}
