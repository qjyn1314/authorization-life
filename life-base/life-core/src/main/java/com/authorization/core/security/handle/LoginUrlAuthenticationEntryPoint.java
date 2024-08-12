package com.authorization.core.security.handle;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.URLUtil;
import com.authorization.utils.security.SsoSecurityProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;
import java.net.URI;

/**
 * 自定义的未授权时所需要跳转的页面
 */
@Slf4j
public class LoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String LOGIN_URL = SsoSecurityProperties.SSO_LOGIN_FORM_PAGE;

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
        if (CharSequenceUtil.isBlank(redirectUri)) {
            redirectStrategy.sendRedirect(request, response, loginPath);
            return;
        }
        String url;
        // 存在重定向参数则拼接地址再重定向
        if (CharSequenceUtil.isBlank(query)) {
            url = loginPath + "?redirect_uri=" + URLUtil.encodeAll(redirectUri);
        } else {
            url = loginPath + "&redirect_uri=" + URLUtil.encodeAll(redirectUri);
        }
        redirectStrategy.sendRedirect(request, response, url);
    }
}
