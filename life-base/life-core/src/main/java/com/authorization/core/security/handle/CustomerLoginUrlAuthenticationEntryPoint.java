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
import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义的未授权时所需要跳转的页面
 */
@Slf4j
public class CustomerLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String LOGIN_URL = SsoSecurityProperties.SSO_LOGIN_FORM_PAGE;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final String loginPath;

    public CustomerLoginUrlAuthenticationEntryPoint() {
        this.loginPath = LOGIN_URL;
    }

    public CustomerLoginUrlAuthenticationEntryPoint(String loginPath) {
        this.loginPath = loginPath;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("进入-未登录处理器-CustomerLoginUrlAuthenticationEntryPoint-请求路径->{}-->重定向地址是->{}", request.getRequestURI(), request.getParameter("redirect_uri"));

        requestInfo(request);

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
