package com.authorization.life.auth.infra.security.handler.sso;

import cn.hutool.core.lang.UUID;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.security.sso.CaptchaWebAuthenticationDetails;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.SecurityCoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 单点登录成功处理类
 */
@Slf4j
public class SsoSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("密码验证登录成功的对象信息是-{}", JSONUtil.toJsonStr(authentication));
        authorizationCodeUrl(request, authentication);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
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

    /**
     * 在登录成功后生成的授权路径
     */
    private void authorizationCodeUrl(HttpServletRequest request, Authentication authentication) {
        String origin = request.getHeader("origin");
        String forwardedPrefix = request.getHeader(SecurityCoreService.AUTH_FORWARDED);
        String baseApi = request.getHeader("base-api");
        String hostOrigin = origin + baseApi + forwardedPrefix.replace("/", "");
        CaptchaWebAuthenticationDetails details = (CaptchaWebAuthenticationDetails) authentication.getDetails();
        OauthClientService clientService = SpringUtil.getBean(OauthClientService.class);
        OauthClientVO oauthClientVO = clientService.getClient(details.getClientId());
        String state = UUID.fastUUID().toString(true);
        String redirectUrl = genAuthorizationCodeUrl(hostOrigin, oauthClientVO.getClientId(),
                oauthClientVO.getScopes(), state, oauthClientVO.getRedirectUri());
        log.info("在登录成功后生成的授权路径是:{}", redirectUrl);
    }


    /**
     * 授权码模式中-生成获取临时code的URL
     *
     * @param hostOrigin  授权服务器域名前缀,例如: <a href="http://dev.authorization.life/dev-api/auth-life">http://dev.authorization.life/dev-api/auth-life</a>
     * @param clientId    客户端
     * @param scope       授权域
     * @param redirectUri 客户端的回调路径
     * @return String
     */
    String genAuthorizationCodeUrl(String hostOrigin, String clientId, String scope, String state, String redirectUri) {
        return hostOrigin + UriComponentsBuilder
                .fromPath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

}
