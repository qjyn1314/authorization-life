package com.authorization.core.intercept;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UrlInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("UrlInfoInterceptor preHandle");
        try {
            //此处为了输出一些请求中的参数信息
            requestInfo(request);
        } catch (Exception e) {
            log.warn("错误信息->", e);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("UrlInfoInterceptor postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("UrlInfoInterceptor afterCompletion");
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
