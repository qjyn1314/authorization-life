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
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

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
        log.warn("进入-未登录处理器-CustomerLoginUrlAuthenticationEntryPoint-请求路径->{}-->重定向地址是->{}", request.getRequestURI(), request.getParameter("redirect_uri"));
        String loginFormUrl = determineUrlToUseForThisRequest(request, response, authException);
        log.info("loginFormUrl={}", loginFormUrl);
        try {
            //此处为了输出一些请求中的参数信息
            requestInfo(request);
        } catch (Exception e) {
            log.warn("错误信息->", e);
        }
        String redirectUri = request.getParameter("redirect_uri");
        // 直接重定向到登录页面
        if (CharSequenceUtil.isBlank(redirectUri)) {
            redirectStrategy.sendRedirect(request, response, loginFormUrl);
            return;
        }
        StringBuffer requestUrl = request.getRequestURL();
        log.info("requestUrl={}", requestUrl);

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

    private void requestInfo(HttpServletRequest request) {
        String forwardedHost = request.getHeader("x-forwarded-host");
        log.info("forwardedHost->{}", forwardedHost);
        String forwarded = request.getHeader("forwarded");
        log.info("forwarded->{}", forwarded);
        //  header->forwarded-headerVal->proto=http;host=passport-dev.authorization.life;for="127.0.0.1:49971"
        Map<String, String> forwardedMap = Arrays.stream(forwarded.split(";"))
                .collect(Collectors.toMap(key -> key.split("=")[0],
                        value -> value.split("=")[1]));
        String proto = forwardedMap.getOrDefault("proto", "");
        String host = forwardedMap.getOrDefault("host", "");
        log.info("proto->{}", proto);
        log.info("host->{}", host);

        String[] forwardedSplit = forwarded.split(";");
        for (int i = 0; i < forwardedSplit.length; i++) {
            String key = forwardedSplit[0].split("=")[0];
            String value = forwardedSplit[0].split("=")[1];
        }

        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String header = iterator.next();
            String headerVal = request.getHeader(header);
            log.info("header->{}-headerVal->{}", header, headerVal);
        }
        String contextPath = request.getContextPath();
        log.info("contextPath->{}", contextPath);
        String servletPath = request.getServletPath();
        log.info("servletPath->{}", servletPath);
        String remoteAddr = request.getRemoteAddr();
        log.info("remoteAddr->{}", remoteAddr);
        String remoteHost = request.getRemoteHost();
        log.info("remoteHost->{}", remoteHost);
        int remotePort = request.getRemotePort();
        log.info("remotePort->{}", remotePort);
        String localAddr = request.getLocalAddr();
        log.info("localAddr->{}", localAddr);
        String localName = request.getLocalName();
        log.info("localName->{}", localName);
        int localPort = request.getLocalPort();
        log.info("localPort->{}", localPort);
        String serverName = request.getServerName();
        log.info("serverName->{}", serverName);
        int serverPort = request.getServerPort();
        log.info("serverPort->{}", serverPort);
        String scheme = request.getScheme();
        log.info("scheme->{}", scheme);

        Principal userPrincipal = request.getUserPrincipal();
        log.info("userPrincipal->{}", userPrincipal);

    }
}
