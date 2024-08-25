package com.authorization.life.auth.infra.security.service;

import com.authorization.utils.security.SecurityCoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行缓存  OAuth2Authorization
 */
@Slf4j
@RequiredArgsConstructor
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private final static Long TIMEOUT = 10L;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        if (isState(authorization)) {
            String token = authorization.getAttribute("state");
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue()
                    .set(buildKey(OAuth2ParameterNames.STATE, token), authorization, TIMEOUT, TimeUnit.MINUTES);
            redisTemplate.opsForValue()
                    .set(buildKeyId(authorization.getId()), authorization, TIMEOUT, TimeUnit.MINUTES);
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                    .getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            long between = ChronoUnit.MINUTES.between(authorizationCodeToken.getIssuedAt(),
                    authorizationCodeToken.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue()
                    .set(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()), authorization,
                            between, TimeUnit.MINUTES);
            redisTemplate.opsForValue()
                    .set(buildKeyId(authorization.getId()), authorization,
                            between, TimeUnit.MINUTES);
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            long between = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue()
                    .set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), authorization, between,
                            TimeUnit.SECONDS);
            redisTemplate.opsForValue()
                    .set(buildKeyId(authorization.getId()), authorization, between,
                            TimeUnit.SECONDS);
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            long between = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue()
                    .set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), authorization, between,
                            TimeUnit.SECONDS);
            redisTemplate.opsForValue()
                    .set(buildKeyId(authorization.getId()), authorization, between,
                            TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        Set<String> keys = new HashSet<>();
        if (isState(authorization)) {
            String token = authorization.getAttribute("state");
            keys.add(buildKey(OAuth2ParameterNames.STATE, token));
            keys.add(buildKeyId(authorization.getId()));
        }

        if (isCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                    .getToken(OAuth2AuthorizationCode.class);
            OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
            keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
            keys.add(buildKeyId(authorization.getId()));
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
            keys.add(buildKeyId(authorization.getId()));
        }

        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
            keys.add(buildKeyId(authorization.getId()));
        }
        redisTemplate.delete(keys);
    }

    @Override
    @Nullable
    public OAuth2Authorization findById(String id) {
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKeyId(id));
    }

    @Override
    @Nullable
    public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return (OAuth2Authorization) redisTemplate.opsForValue().get(buildKey(tokenType.getValue(), token));
    }

    private String buildKeyId(String id) {
        return String.format(SecurityCoreService.AUTHORIZATION_KET_PREFIX + "%s", id);
    }

    private String buildKey(String type, String id) {
        return String.format(SecurityCoreService.AUTHORIZATION_KET_PREFIX + "%s:%s", type, id);
    }

    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute("state"));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        return Objects.nonNull(authorizationCode);
    }

    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getRefreshToken());
    }

    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAccessToken());
    }

}
