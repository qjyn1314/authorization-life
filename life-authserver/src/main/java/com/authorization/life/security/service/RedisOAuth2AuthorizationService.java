package com.authorization.life.security.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.authorization.core.security.SecurityConstant;
import com.authorization.redis.start.util.RedisConsts;
import com.authorization.redis.start.util.StrRedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 使用redis进行缓存  OAuth2Authorization
 */
@Slf4j
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String AUTHORIZATION = SecurityConstant.AUTHORIZATION;
    public static final String UNDERSCORE = "_";
    private final static String AUTHORIZATION_UNDERSCORE = AUTHORIZATION + UNDERSCORE;

    private final RedisTemplate<String, Object> redisTemplate;
    private final StrRedisHelper strRedisHelper;

    public RedisOAuth2AuthorizationService(RedisTemplate<String, Object> redisTemplate, StrRedisHelper strRedisHelper) {
        this.redisTemplate = redisTemplate;
        this.strRedisHelper = strRedisHelper;
    }


    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        String authId = AUTHORIZATION_UNDERSCORE + authorization.getId();
        redisTemplate.opsForValue().set(authId, authorization, 1, TimeUnit.DAYS);
        //将授权信息以token为key存入缓存
        saveTokenToCache(authorization);
    }

    private void saveTokenToCache(@NotNull OAuth2Authorization authorization) {
        String authId = authorization.getId();
        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set格式放入缓存(摘要算法可能重复)，过期时间为一天
        if (StrUtil.isNotBlank(accessToken)) {
            String accessTokenKey = AUTHORIZATION_UNDERSCORE + OAuth2TokenType.ACCESS_TOKEN.getValue() + UNDERSCORE + DigestUtil.md5Hex(accessToken);
            strRedisHelper.setAdd(accessTokenKey, authId);
            strRedisHelper.setExpire(accessTokenKey, RedisConsts.DEFAULT_EXPIRE);
        }
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StrUtil.isNotBlank(refreshToken)) {
            String refreshTokenKey = AUTHORIZATION_UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + UNDERSCORE + DigestUtil.md5Hex(refreshToken);
            strRedisHelper.setAdd(refreshTokenKey, authId);
            strRedisHelper.setExpire(refreshTokenKey, RedisConsts.DEFAULT_EXPIRE);
        }
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StrUtil.isNotBlank(authCodeToken)) {
            String authCodeTokenKey = AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.CODE + UNDERSCORE + DigestUtil.md5Hex(authCodeToken);
            strRedisHelper.setAdd(authCodeTokenKey, authId);
            strRedisHelper.setExpire(authCodeTokenKey, RedisConsts.DEFAULT_EXPIRE);
        }
        String state = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StrUtil.isNotBlank(state)) {
            String stateKey = AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.STATE + UNDERSCORE + DigestUtil.md5Hex(state);
            strRedisHelper.setAdd(stateKey, authId);
            strRedisHelper.setExpire(stateKey, RedisConsts.DEFAULT_EXPIRE);
        }
    }


    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        strRedisHelper.delKey(AUTHORIZATION_UNDERSCORE + authorization.getId());
        //删除以token为key的数据
        removeTokenToCache(authorization);
    }

    private void removeTokenToCache(OAuth2Authorization authorization) {
        String authId = authorization.getId();
        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set结构中将对应value值删除
        if (StrUtil.isNotBlank(accessToken)) {
            strRedisHelper.hashDelete(AUTHORIZATION_UNDERSCORE + OAuth2TokenType.ACCESS_TOKEN.getValue() + DigestUtil.md5Hex(accessToken), authId);
        }
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StrUtil.isNotBlank(refreshToken)) {
            strRedisHelper.hashDelete(AUTHORIZATION_UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + DigestUtil.md5Hex(refreshToken), authId);
        }
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StrUtil.isNotBlank(authCodeToken)) {
            strRedisHelper.hashDelete(AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.CODE + DigestUtil.md5Hex(authCodeToken), authId);
        }
        String attribute = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StrUtil.isNotBlank(attribute)) {
            strRedisHelper.hashDelete(AUTHORIZATION_UNDERSCORE + OAuth2ParameterNames.STATE + DigestUtil.md5Hex(attribute), authId);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return (OAuth2Authorization) redisTemplate.opsForValue().get(AUTHORIZATION_UNDERSCORE + id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        OAuth2Authorization authByCacheToken = null;
        //如果没有token类型,则对于4个类型挨个匹配
        if (tokenType == null) {
            if ((authByCacheToken = getAuthByCacheToken(token, OAuth2ParameterNames.STATE)) == null &&
                    (authByCacheToken = getAuthByCacheToken(token, OAuth2ParameterNames.CODE)) == null &&
                    (authByCacheToken = getAuthByCacheToken(token, OAuth2TokenType.ACCESS_TOKEN.getValue())) == null &&
                    (authByCacheToken = getAuthByCacheToken(token, OAuth2TokenType.REFRESH_TOKEN.getValue())) == null) {
                return null;
            } else {
                return authByCacheToken;
            }
        } else {
            authByCacheToken = getAuthByCacheToken(token, tokenType);
        }
        return authByCacheToken;
    }

    private static boolean hasToken(OAuth2Authorization authorization, String token, @Nullable String tokenType) {
        if (tokenType == null) {
            return matchesState(authorization, token) ||
                    matchesAuthorizationCode(authorization, token) ||
                    matchesAccessToken(authorization, token) ||
                    matchesRefreshToken(authorization, token);
        } else if (OAuth2ParameterNames.STATE.equals(tokenType)) {
            return matchesState(authorization, token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType)) {
            return matchesAuthorizationCode(authorization, token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.getValue().equals(tokenType)) {
            return matchesAccessToken(authorization, token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.getValue().equals(tokenType)) {
            return matchesRefreshToken(authorization, token);
        }
        return false;
    }

    private static boolean matchesState(OAuth2Authorization authorization, String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
    }

    private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
    }

    private String getTokenByAuth(OAuth2Authorization authorization, String tokenType) {
        return authorization.getAttribute(tokenType);
    }

    private String getTokenByAuth(OAuth2Authorization authorization, Class<? extends OAuth2Token> tokenType) {
        OAuth2Authorization.Token<?> accessToken = authorization.getToken(tokenType);
        if (Objects.isNull(accessToken)) {
            return null;
        }
        return accessToken.getToken().getTokenValue();
    }

    private OAuth2Authorization getAuthByCacheToken(String token, Object tokenType) {
        String type = null;
        if (tokenType instanceof OAuth2TokenType) {
            type = ((OAuth2TokenType) tokenType).getValue();
        } else {
            type = (String) tokenType;
        }
        //从set集合中取出值,因为key为摘要算法生产，可能存在相同key，所以值可能有多个
        Set<String> authIdSet = strRedisHelper.setMembers(AUTHORIZATION_UNDERSCORE + type + UNDERSCORE + DigestUtil.md5Hex(token));
        if (CollUtil.isEmpty(authIdSet)) {
            return null;
        }
        List<String> authList = new ArrayList<>(authIdSet);
        //当值集合只有1个时，直接取对应的权限信息
        if (authList.size() == 1) {
            return (OAuth2Authorization) redisTemplate.opsForValue().get(AUTHORIZATION_UNDERSCORE + authList.get(0));
        } else {
            //如果有多个，则需要循环key下的所有值，判断token是否与取得对权限中对应类型token相同，相同则返回
            for (Object authId : authList) {
                OAuth2Authorization authorization = (OAuth2Authorization) redisTemplate.opsForValue().get(AUTHORIZATION_UNDERSCORE + authId);
                if (hasToken(authorization, token, type)) {
                    return authorization;
                }
            }
        }
        return null;
    }
}
