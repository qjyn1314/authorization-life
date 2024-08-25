package com.authorization.life.auth.infra.security.handler.sso;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.exception.handle.DefaultErrorMsg;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.SsoSecurityProperties;
import com.authorization.utils.security.UserDetail;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 退出登录处理类
 */
@Slf4j
public class SsoLogoutHandle implements LogoutHandler {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final RedisUtil redisUtil;
    private final SsoSecurityProperties ssoSecurityProperties;
    private final JwtService jwtService;

    public SsoLogoutHandle(OAuth2AuthorizationService oAuth2AuthorizationService, RedisUtil redisUtil, SsoSecurityProperties ssoSecurityProperties, JwtService jwtService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.redisUtil = redisUtil;
        this.ssoSecurityProperties = ssoSecurityProperties;
        this.jwtService = jwtService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("进入退出登录处理器。");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        // 前端传参的 accesstoken
        String authorization = request.getHeader(SsoSecurityProperties.AUTHORIZATION);
        // 网关层次解析后的 jwtToken
        String interiorJwt = request.getHeader(SsoSecurityProperties.AUTH_POSITION);
        log.debug("请求头-ACCESS_TOKEN-是：" + authorization);
        log.debug("请求头-Auth-Position-是：" + interiorJwt);
        if (StrUtil.isBlank(authorization) || StrUtil.isBlank(interiorJwt)) {
            try {
                PrintWriter out = response.getWriter();
                out.write(JSONUtil.toJsonStr(Result.failCode(Result.ERROR, DefaultErrorMsg.TOKEN_OF_HEADER_NOT_FOUND.getMsgCode())));
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("退出登录处理器处理失败，获取不到请求头-Authorization的值", e);
            }
        }
        // 此处重新解析是由于 SsoLogoutHandle 在  JwtAuthenticationFilter 之前执行,此时并没有将当前登录用户设置到 SecurityContextHolder 中.
        UserDetail userDetail = BeanUtil.toBean(jwtService.getClaimsFromJwtToken(interiorJwt), UserDetail.class);
        log.debug("当前登录用户-UserDetail-是：" + userDetail);
        // 先删除 Oauth2Server中存在于redis的accessToken, 包含 code,accessToken,refreshToken
        if (StrUtil.isNotBlank(userDetail.getAccessTokenId())) {
            OAuth2Authorization auth2Authorization = oAuth2AuthorizationService.findById(userDetail.getAccessTokenId());
            oAuth2AuthorizationService.remove(auth2Authorization);
        }
        //再删除根据token获取用户信息
        if (StrUtil.isNotBlank(userDetail.getToken())) {
            redisUtil.delete(SecurityCoreService.getUserTokenKey(userDetail.getToken()));
        }
        //清除掉当前登录用户的信息.
        SecurityContextHolder.clearContext();
        //清除cookie中的数据
        Cookie cookieAccessToken = new Cookie(SsoSecurityProperties.ACCESS_TOKEN, null);
        cookieAccessToken.setMaxAge(0);
        cookieAccessToken.setPath("/");
        Cookie cookieTokenType = new Cookie(SsoSecurityProperties.TOKEN_TYPE, null);
        cookieTokenType.setMaxAge(0);
        cookieTokenType.setPath("/");
        Cookie cookieJsessionid = new Cookie(SsoSecurityProperties.JSESSIONID, null);
        cookieJsessionid.setMaxAge(0);
        cookieJsessionid.setPath("/");
        response.addCookie(cookieAccessToken);
        response.addCookie(cookieTokenType);
        response.addCookie(cookieJsessionid);
        try {
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr(Result.ok()));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("退出登录处理器处理失败，", e);
        }
    }
}
