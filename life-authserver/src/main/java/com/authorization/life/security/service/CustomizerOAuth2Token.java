package com.authorization.life.security.service;


import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.authorization.core.entity.UserDetail;
import com.authorization.life.entity.User;
import com.authorization.life.service.OauthClientService;
import com.authorization.redis.start.util.StrRedisHelper;
import com.authorization.start.util.contsant.LifeSecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.ProviderContext;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义accesstoken的实现，设置 accesstoken 的形式是jwt，定义JWT的header和 claims 。指定一个uuid作为key，并将用户相关的信息存放至redis中，
 */
@Slf4j
public class CustomizerOAuth2Token implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final SecurityAuthUserService securityAuthUserService;
    private final OauthClientService oauthClientService;
    private final StrRedisHelper strRedisHelper;


    public CustomizerOAuth2Token(SecurityAuthUserService securityAuthUserService,
                                 OauthClientService oauthClientService,
                                 StrRedisHelper strRedisHelper) {
        this.securityAuthUserService = securityAuthUserService;
        this.oauthClientService = oauthClientService;
        this.strRedisHelper = strRedisHelper;
    }

    /**
     * 将重写jwtToken的中的信息，并将其存储到redis中。
     *
     * @param context JwtEncodingContext
     */
    @Override
    public void customize(JwtEncodingContext context) {
        //此处的token字符串是前端拿到的jwtToken信息中解密后的字符串，在这里将自定义jwtToken的实现，将定制jwt的 header 和 claims，将此token存放到 claim 中
        String token = UUID.randomUUID().toString(true);
        Authentication principal = context.getPrincipal();
        Authentication authorizationGrant = context.getAuthorizationGrant();
        OAuth2Authorization authorization = context.getAuthorization();
        Set<String> authorizedScopes = context.getAuthorizedScopes();
        ProviderContext providerContext = context.getProviderContext();
        RegisteredClient registeredClient = context.getRegisteredClient();
        log.info("principal-{}", JSONUtil.toJsonStr(principal));
        log.info("authorization-{}", JSONUtil.toJsonStr(authorization));
        log.info("authorizedScopes-{}", JSONUtil.toJsonStr(authorizedScopes));
        log.info("authorizationGrant-{}", JSONUtil.toJsonStr(authorizationGrant));
        log.info("providerContext-{}", JSONUtil.toJsonStr(providerContext));
        log.info("registeredClient-{}", JSONUtil.toJsonStr(registeredClient));
        UserDetail userDetail = null;
        // 目的是为了定制jwt 的header 和 claims
        if (principal instanceof OAuth2ClientAuthenticationToken) {
            //如果当前登录的是client，则进行封装client
//            userDetail = securityAuthUserService.createUserDetailByClientId(registeredClient.getClientId());
        }
//        else if (principal.getPrincipal() instanceof UserDetail) {
//            //如果当前登录的是系统用户，则进行封装userDetail
//            userDetail = securityAuthUserService.createUserDetailByUser((UserDetails) principal.getPrincipal());
//        }
        else if (principal.getPrincipal() instanceof User) {
            //如果当前登录的是系统用户，则进行封装userDetail
            userDetail = securityAuthUserService.createUserDetailByUser((User) principal.getPrincipal());
        }
        //如果解析失败，则抛出异常信息。
        if (Objects.isNull(userDetail)) {
            log.error("在自定义token实现中, 用户信息解析异常。");
            userDetail = new UserDetail();
        }

        //也需要将此token存放到当前登录用户中，为了在退出登录时进行获取redis中的信息并将其删除
        userDetail.setToken(token);
        //将用户信息放置到redis中，并设置其过期时间为 client中的过期时间
        strRedisHelper.strSet(LifeSecurityConstants.getUserTokenKey(token), userDetail,
                registeredClient.getTokenSettings().getAccessTokenTimeToLive().getSeconds(), TimeUnit.SECONDS);
        log.info("生成的用户-token是-{}，此token作为key，用户信息作为value存储到redis中", token);
        //也可以在此处将当前登录用户的信息存放到jwt中，但是这样就不再安全。
        context.getClaims().claim(LifeSecurityConstants.TOKEN, token).build();
    }
}
