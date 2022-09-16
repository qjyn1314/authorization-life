package com.authorization.life.security.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.authorization.core.security.SecurityConstant;
import com.authorization.redis.start.util.ObjRedisHelper;
import com.authorization.redis.start.util.RedisConsts;
import org.jetbrains.annotations.NotNull;
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
public final class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final String AUTHORIZATION = SecurityConstant.AUTHORIZATION;

    private final static String UNDERSCORE = "_";

    private final ObjRedisHelper objRedisHelper;

    public RedisOAuth2AuthorizationService(ObjRedisHelper objRedisHelper) {
        this.objRedisHelper = objRedisHelper;
    }


    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        objRedisHelper.setEx(AUTHORIZATION + "_" + authorization.getId(), authorization, 1, TimeUnit.DAYS);
        //将授权信息以token为key存入缓存
        saveTokenToCache(authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        objRedisHelper.del(AUTHORIZATION + UNDERSCORE + authorization.getId());
        //删除以token为key的数据
        removeTokenToCache(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return objRedisHelper.get(AUTHORIZATION + UNDERSCORE + id);
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

    private void saveTokenToCache(@NotNull OAuth2Authorization authorization) {
        String authId = authorization.getId();
        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set格式放入缓存(摘要算法可能重复)，过期时间为一天
        if (StrUtil.isNotBlank(accessToken)) {
            String accessTokenKey = AUTHORIZATION + UNDERSCORE + OAuth2TokenType.ACCESS_TOKEN.getValue() + UNDERSCORE + DigestUtil.md5Hex(accessToken);
            objRedisHelper.sAdd(accessTokenKey, authId);
            objRedisHelper.expire(accessTokenKey, RedisConsts.DEFAULT_EXPIRE);
        }
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StrUtil.isNotBlank(refreshToken)) {
            objRedisHelper.sAdd(AUTHORIZATION + UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + UNDERSCORE + DigestUtil.md5Hex(refreshToken), authId);
            objRedisHelper.expire(AUTHORIZATION + UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + UNDERSCORE + DigestUtil.md5Hex(refreshToken), RedisConsts.DEFAULT_EXPIRE);
        }
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StrUtil.isNotBlank(authCodeToken)) {
            objRedisHelper.sAdd(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.CODE + UNDERSCORE + DigestUtil.md5Hex(authCodeToken), authId);
            objRedisHelper.expire(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.CODE + UNDERSCORE + DigestUtil.md5Hex(authCodeToken), RedisConsts.DEFAULT_EXPIRE);
        }
        String attribute = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StrUtil.isNotBlank(attribute)) {
            objRedisHelper.sAdd(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.STATE + UNDERSCORE + DigestUtil.md5Hex(attribute), authId);
            objRedisHelper.expire(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.STATE + UNDERSCORE + DigestUtil.md5Hex(attribute), RedisConsts.DEFAULT_EXPIRE);
        }

    }

    private void removeTokenToCache(OAuth2Authorization authorization) {
        String authId = authorization.getId();
        String accessToken = getTokenByAuth(authorization, OAuth2AccessToken.class);
        //判断该授权信息中4个类型的token是否不为空，如果有值，则需要把对应token类型+MD5(token)为key,授权id为value的set结构中将对应value值删除
        if (StrUtil.isNotBlank(accessToken)) {
            objRedisHelper.sRem(AUTHORIZATION + UNDERSCORE + OAuth2TokenType.ACCESS_TOKEN.getValue() + DigestUtil.md5Hex(accessToken), authId);
        }
        String refreshToken = getTokenByAuth(authorization, OAuth2RefreshToken.class);
        if (StrUtil.isNotBlank(refreshToken)) {
            objRedisHelper.sRem(AUTHORIZATION + UNDERSCORE + OAuth2TokenType.REFRESH_TOKEN.getValue() + DigestUtil.md5Hex(refreshToken), authId);
        }
        String authCodeToken = getTokenByAuth(authorization, OAuth2AuthorizationCode.class);
        if (StrUtil.isNotBlank(authCodeToken)) {
            objRedisHelper.sRem(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.CODE + DigestUtil.md5Hex(authCodeToken), authId);
        }
        String attribute = getTokenByAuth(authorization, OAuth2ParameterNames.STATE);
        if (StrUtil.isNotBlank(attribute)) {
            objRedisHelper.sRem(AUTHORIZATION + UNDERSCORE + OAuth2ParameterNames.STATE + DigestUtil.md5Hex(attribute), authId);
        }
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
        Set<Object> authIdSet = objRedisHelper.sMembers(AUTHORIZATION + UNDERSCORE + type + UNDERSCORE + DigestUtil.md5Hex(token));
        if (CollUtil.isEmpty(authIdSet)) {
            return null;
        }
        List<Object> authList = new ArrayList<>(authIdSet);
        //当值集合只有1个时，直接取对应的权限信息
        if (authList.size() == 1) {
            return objRedisHelper.get(AUTHORIZATION + UNDERSCORE + authList.get(0));
        } else {
            //如果有多个，则需要循环key下的所有值，判断token是否与取得对权限中对应类型token相同，相同则返回
            for (Object authId : authList) {
                OAuth2Authorization authorization = objRedisHelper.get(AUTHORIZATION + UNDERSCORE + authId);
                if (hasToken(authorization, token, type)) {
                    return authorization;
                }
            }
        }
        return null;
    }
}
