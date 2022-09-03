package com.authorization.life.security.handler.oauth;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.core.security.SecurityConstant;
import com.authorization.start.util.format.MsgFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * oauth授权成功之后处理器
 */
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("重定向default auth令牌");
        OAuth2AuthorizationCodeRequestAuthenticationToken codeRequestAuthenticationToken =
                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        if (authentication.getPrincipal() instanceof OAuth2AccessTokenAuthenticationToken){
            OAuth2AccessTokenAuthenticationToken token = (OAuth2AccessTokenAuthenticationToken)authentication.getPrincipal();
            OAuth2AccessToken accessToken = token.getAccessToken();
            String redirectUrl = MsgFormat.of(SecurityConstant.IMPLICIT_REDIRECT_URI_FORMAT)
                    .add("redirectUri",codeRequestAuthenticationToken.getRedirectUri())
                    .add("accessToken", accessToken.getTokenValue())
                    .add("tokenType", accessToken.getTokenType().getValue())
                    .add("expiresIn", Duration.between(Instant.now(), accessToken.getExpiresAt()).getSeconds())
                    .add("state", codeRequestAuthenticationToken.getState())
                    .add("scope", String.join(StrUtil.COMMA, accessToken.getScopes()))
                    .format();
            response.sendRedirect(redirectUrl);
        }else {
            sendAccessTokenResponse(request, response, authentication);
        }

    }

    private void sendAccessTokenResponse(HttpServletRequest request, HttpServletResponse response,
                                         Authentication authentication) throws IOException {

        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
        Assert.notNull(authorizationCodeRequestAuthentication.getRedirectUri(), "回调地址不能为空。");
        Assert.notNull(authorizationCodeRequestAuthentication.getAuthorizationCode(), "认证授权码不能为空。");
        Assert.notNull(authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue(), "授权码的token不能为空。");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(authorizationCodeRequestAuthentication.getRedirectUri())
                .queryParam(OAuth2ParameterNames.CODE, authorizationCodeRequestAuthentication.getAuthorizationCode().getTokenValue());
        if (StringUtils.hasText(authorizationCodeRequestAuthentication.getState())) {
            uriBuilder.queryParam(OAuth2ParameterNames.STATE, authorizationCodeRequestAuthentication.getState());
        }
        this.redirectStrategy.sendRedirect(request, response, uriBuilder.toUriString());
    }
}
