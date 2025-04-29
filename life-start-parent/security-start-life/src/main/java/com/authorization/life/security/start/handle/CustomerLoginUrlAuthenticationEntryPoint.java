package com.authorization.life.security.start.handle;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.io.IOException;
import java.net.URI;

/**
 * 自定义的未授权时所需要跳转的页面
 */
@Slf4j
public class CustomerLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public CustomerLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("进入-未登录处理器-CustomerLoginUrlAuthenticationEntryPoint-请求路径->{}-->重定向地址是->{}",
                request.getRequestURI(), request.getParameter("redirect_uri"));
        String loginFormUrl = determineUrlToUseForThisRequest(request, response, authException);
        log.info("loginFormUrl={}", loginFormUrl);
        String redirectUri = request.getParameter("redirect_uri");
        // 没有回调页时, 直接重定向到登录页面
        if (CharSequenceUtil.isBlank(redirectUri)) {
            redirectStrategy.sendRedirect(request, response, loginFormUrl);
            return;
        }

        String loginPageUrl = loginFormUrl + "?redirect_uri=" + redirectUri;

        try {
            URI uri = URI.create(loginPageUrl);
        } catch (Exception e) {
            log.error("登录页解析失败", e);
            throw new IllegalArgumentException("登录页解析失败.");
        }
        log.info("loginPageUrl={}", loginPageUrl);
        redirectStrategy.sendRedirect(request, response, loginPageUrl);
    }

}
