package com.authorization.life.auth.infra.security.handler.sso;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.entity.UserDetail;
import com.authorization.core.entity.UserHelper;
import com.authorization.core.security.SecurityConstant;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.kvp.KvpFormat;
import com.authorization.utils.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 退出登录处理类
 */
@Slf4j
public class SsoLogoutHandle implements LogoutHandler {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final StringRedisService stringRedisService;

    public SsoLogoutHandle(OAuth2AuthorizationService oAuth2AuthorizationService, StringRedisService stringRedisService) {
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.stringRedisService = stringRedisService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("进入退出登录处理器。");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        UserDetail userDetail = UserHelper.getUserDetail();
        log.debug("当前登录用户-UserDetail-是：" + userDetail);
        if (Objects.nonNull(userDetail)) {
            String userToken = userDetail.getToken();
            log.debug("当前登录用户的token-是：" + userToken);
            String cacheUserToken = KvpFormat.of(SecurityConstant.USER_DETAIL).add("token", userToken).format();
            stringRedisService.delKey(cacheUserToken);
            stringRedisService.delKey(KvpFormat.of(SecurityConstant.TOKEN_STORE).add("userId", userDetail.getUserId().toString()).format());
        }
        SecurityContextHolder.clearContext();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("请求头-Authorization-是：" + token);
        try {
            if (StrUtil.isBlank(token)) {
                PrintWriter out = response.getWriter();
                out.write(JSONUtil.toJsonStr(new R<>(R.ERROR, "未找到token，请确认已登录。", null)));
                out.flush();
                out.close();
            }
            token = token.split(" ")[1];
            OAuth2Authorization auth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
            log.debug("查询出来-OAuth2Authorization-是：" + JSONUtil.toJsonStr(auth2Authorization));
            if (Objects.nonNull(auth2Authorization)) {
                oAuth2AuthorizationService.remove(auth2Authorization);
            }
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr(new R<>(R.SUCCESS, R.SUCCESS_DESC, null)));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("退出登录处理器处理失败，", e);
        }
    }
}
