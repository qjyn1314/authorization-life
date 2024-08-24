package com.authorization.life.auth.infra.security.service;


import cn.hutool.core.lang.UUID;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.infra.security.sso.CustomizerTokenException;
import com.authorization.redis.start.util.RedisService;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 自定义accesstoken的实现，设置 accesstoken 的形式是jwt，定义JWT的header和 claims 。指定一个uuid作为key，并将用户相关的信息存放至redis中，
 */
@Slf4j
public class CustomizerOAuth2Token implements OAuth2TokenCustomizer<JwtEncodingContext> {

    private final SecurityAuthUserService securityAuthUserService;
    private final OauthClientService oauthClientService;
    private final RedisService stringRedisService;
    private final HttpServletRequest servletRequest;

    public CustomizerOAuth2Token(SecurityAuthUserService securityAuthUserService, OauthClientService oauthClientService,
                                 RedisService stringRedisService, HttpServletRequest servletRequest) {
        this.securityAuthUserService = securityAuthUserService;
        this.oauthClientService = oauthClientService;
        this.stringRedisService = stringRedisService;
        this.servletRequest = servletRequest;
    }

    /**
     * 目的是为了定制jwt 的header 和 claims,
     * 将重写jwtToken的中的信息，并将其存储到redis中。
     *
     * @param context JwtEncodingContext
     */
    @Override
    public void customize(JwtEncodingContext context) {
        OAuth2TokenType tokenType = context.getTokenType();

        JwsHeader.Builder jwsHeader = context.getJwsHeader();
        Authentication principal = context.getPrincipal();
        log.warn("进入了自定义token实现类,认证信息是-->{}", principal);
        RegisteredClient registeredClient = context.getRegisteredClient();
        UserDetail userDetail = null;
        if (principal instanceof OAuth2ClientAuthenticationToken) {
            //如果当前登录的是client，则进行封装client
            userDetail = securityAuthUserService.createUserDetailByClientId(registeredClient.getClientId());
        } else if (principal.getPrincipal() instanceof UserDetails) {
            OAuth2Authorization authorization = context.getAuthorization();
            //如果当前登录的是系统用户，则进行封装userDetail
            userDetail = securityAuthUserService.createUserDetailByUser((UserDetails) principal.getPrincipal());
            if (Objects.nonNull(authorization)) {
                // 将 RedisOAuth2AuthorizationConsentService 的  OAuth2Authorization id 存储到当前登录用户信息中,在退出登录时将进行删除
                userDetail.setAuthorizationId(authorization.getId());
                // 通过此 redisKey 也可以获取到当前登录用户的信息, 其实就是 OAuth2Authorization 的信息
                userDetail.setAuthorizationIdToken(SecurityCoreService.getAuthorizationId(authorization.getId()));
            }
        }
        //如果解析失败，则抛出异常信息。
        if (Objects.isNull(userDetail)) {
            log.error("在自定义token实现中, 用户信息解析异常.");
            throw new CustomizerTokenException("生成用户token信息失败.");
        }
        //此处的token字符串是前端拿到的jwtToken信息中解密后的字符串，在这里将自定义jwtToken的实现，将定制jwt的 header 和 claims，将此token存放到 claim 中
        String token = UUID.randomUUID().toString(true);
        //也需要将此token存放到当前登录用户中，为了在退出登录时进行获取redis中的信息并将其删除
        userDetail.setToken(token);
        //将用户信息放置到redis中，并设置其过期时间为 client中的过期时间
        String userTokenKey = SecurityCoreService.getUserTokenKey(token);
        // token过期的秒数
        long tokenOverdueSeconds = registeredClient.getTokenSettings().getAccessTokenTimeToLive().getSeconds();
        log.info("生成gateway服务解析redis存储jwtToken的key是:{},过期时间是:{}秒,默认是 86400秒(24小时) ,此token作为key，用户信息作为value存储到redis中", userTokenKey, tokenOverdueSeconds);
        stringRedisService.strSet(userTokenKey, userDetail, tokenOverdueSeconds, TimeUnit.SECONDS);
        //也可以在此处将当前登录用户的信息存放到jwt中，但是这样就不再安全。
        context.getClaims().claim(SecurityCoreService.CLAIM_TOKEN_KEY, token).build();
    }
}
