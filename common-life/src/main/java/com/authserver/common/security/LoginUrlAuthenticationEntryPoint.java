package com.authserver.common.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

/**
 * 未登录时所做的操作，进行转发到指定路径
 */
@Slf4j
public class LoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String LOGIN_URL = "/login";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final String loginPath;

    public LoginUrlAuthenticationEntryPoint() {
        this.loginPath = LOGIN_URL;
    }

    public LoginUrlAuthenticationEntryPoint(String loginPath) {
        this.loginPath = loginPath;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        URI uri = URI.create(loginPath);
        String query = uri.getQuery();
        String redirectUri = request.getParameter("redirect_uri");
        // 没有重定向参数则直接重定向到登录页面
        if (StrUtil.isBlank(redirectUri)) {
            redirectStrategy.sendRedirect(request, response, loginPath);
            return;
        }
        String url;
        // 存在重定向参数则拼接地址再重定向
        if (StrUtil.isBlank(query)) {
            url = loginPath + "?redirect_uri=" + URLUtil.encodeAll(redirectUri);
        } else {
            url = loginPath + "&redirect_uri=" + URLUtil.encodeAll(redirectUri);
        }
        redirectStrategy.sendRedirect(request, response, url);
    }
}
