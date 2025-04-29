package com.authorization.life.auth.infra.security.handler.sso;

import cn.hutool.core.lang.UUID;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.security.sso.CaptchaWebAuthenticationDetails;
import com.authorization.life.security.start.entity.UserHelper;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.UserDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 单点登录成功处理类
 */
@Slf4j
public class SsoSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("密码验证登录成功的对象信息是-{}", JSONUtil.toJsonStr(authentication));
        authorizationCodeUrl(request, authentication);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//        UserHelper.setUserDetail((UserDetail) authentication.getDetails());
        try {
            PrintWriter out = response.getWriter();
            out.write(JSONUtil.toJsonStr(Result.ok(Map
                    .of("authenticated", authentication.isAuthenticated(),
                            "state", UUID.fastUUID().toString(true)))));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("登录成功处理器处理失败，返回数据异常.", e);
        }
    }

    private void authorizationCodeUrl(HttpServletRequest request, Authentication authentication) {
        String origin = request.getHeader("origin");
        String forwardedPrefix = request.getHeader("x-forwarded-prefix");
        String baseApi = request.getHeader("base-api");
        String hostOrigin = origin + baseApi + forwardedPrefix.replace("/", "");
        CaptchaWebAuthenticationDetails details = (CaptchaWebAuthenticationDetails) authentication.getDetails();
        OauthClientService clientService = SpringUtil.getBean(OauthClientService.class);
        OauthClientVO oauthClientVO = clientService.getClient(details.getClientId());
        String state = UUID.fastUUID().toString(true);
        String redirectUrl = SecurityCoreService.genAuthorizationCodeUrl(hostOrigin, oauthClientVO.getClientId(),
                oauthClientVO.getScopes(), state, oauthClientVO.getRedirectUri());
        log.info("onAuthenticationSuccess->genAuthorizationCodeUrl:{}", redirectUrl);
    }
}
