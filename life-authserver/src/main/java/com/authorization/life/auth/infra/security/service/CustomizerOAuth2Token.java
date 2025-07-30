package com.authorization.life.auth.infra.security.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.infra.security.sso.CustomizerTokenException;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义accesstoken的实现，设置 accesstoken 的形式是jwt，定义JWT的header和 claims 。指定一个uuid作为key，并将用户相关的信息存放至redis中，
 */
@Slf4j
public class CustomizerOAuth2Token implements OAuth2TokenCustomizer<JwtEncodingContext> {

  private final SecurityAuthUserService securityAuthUserService;
  private final OauthClientService oauthClientService;
  private final RedisUtil redisUtil;
  private final HttpServletRequest servletRequest;

  public CustomizerOAuth2Token(
      SecurityAuthUserService securityAuthUserService,
      OauthClientService oauthClientService,
      RedisUtil redisUtil,
      HttpServletRequest servletRequest) {
    this.securityAuthUserService = securityAuthUserService;
    this.oauthClientService = oauthClientService;
    this.redisUtil = redisUtil;
    this.servletRequest = servletRequest;
  }

  /**
   * 目的是为了定制jwt 的header 和 claims, 将重写jwtToken的中的信息，并将其存储到redis中。
   *
   * @param context JwtEncodingContext
   */
  @Override
  public void customize(JwtEncodingContext context) {
    OAuth2TokenType tokenType = context.getTokenType();
    log.error("tokenType->{}", tokenType.getValue());
    OAuth2Authorization oAuth2Authorization = context.getAuthorization();
    String accessTokenId = Objects.nonNull(oAuth2Authorization) ? oAuth2Authorization.getId() : "";
    log.error("ACCESS_TOKEN_ID-->{}", accessTokenId);
    // 认证成功的用户信息
    Authentication authentication = context.getPrincipal();
    RegisteredClient registeredClient = context.getRegisteredClient();
    UserDetail userDetail = null;
    if (authentication instanceof OAuth2ClientAuthenticationToken clientAuthenticationToken) {
      log.info("clientAuthenticationToken->{}", clientAuthenticationToken);
      // 如果当前登录的是client，则进行封装client
      userDetail =
          securityAuthUserService.createUserDetailByClientId(registeredClient.getClientId());
    } else if (authentication.getPrincipal() instanceof UserDetails userDetails) {
      // 如果当前登录的是系统用户，则进行封装userDetail
      userDetail = securityAuthUserService.createUserDetailByUser(userDetails);
    }
    // 如果解析失败，则抛出异常信息。
    if (Objects.isNull(userDetail)) {
      log.error("在自定义token实现中, 用户信息解析异常.认证信息是->{}", authentication);
      throw new CustomizerTokenException("自定义JwtToken中的Claims失败.");
    }
    log.info("当前登录用户信息是->{}", JSON.toJSONString(userDetail));
    userDetail.setAccessTokenId(accessTokenId);
    // 此处的token字符串是前端拿到的jwtToken信息中解密后的字符串，在这里将自定义jwtToken的实现，将定制jwt的 header 和 claims，将此token存放到
    // claim 中
    String token = UUID.randomUUID().toString(true);
    // 也需要将此token存放到当前登录用户中，为了在退出登录时进行获取redis中的信息并将其删除
    userDetail.setToken(token);
    // 将用户信息放置到redis中，并设置其过期时间为 client中的过期时间
    String userTokenKey = SecurityCoreService.getUserTokenKey(token);
    // token过期的秒数
    long tokenOverdueSeconds =
        registeredClient.getTokenSettings().getAccessTokenTimeToLive().getSeconds();
    log.info(
        "生成gateway服务解析redis存储jwtToken的key是:{},过期时间是:{}秒,默认是 86400秒(24小时) ,此token作为key，用户信息作为value存储到redis中",
        userTokenKey,
        tokenOverdueSeconds);
    redisUtil.strSet(userTokenKey, userDetail, tokenOverdueSeconds, TimeUnit.SECONDS);
    // 也可以在此处将当前登录用户的信息存放到jwt中，但是这样就不再安全。
    context.getClaims().claim(SecurityCoreService.CLAIM_TOKEN_KEY, token).build();
  }
}
