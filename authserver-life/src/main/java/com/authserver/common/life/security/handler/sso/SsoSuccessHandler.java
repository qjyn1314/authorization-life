package com.authserver.common.life.security.handler.sso;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登录成功处理类
 */
@Slf4j
public class SsoSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        log.info("当前登录成功的用户是：{}", JSONUtil.toJsonStr(securityContext.getAuthentication().getPrincipal()));
        log.debug("密码验证登录成功");
    }
}
