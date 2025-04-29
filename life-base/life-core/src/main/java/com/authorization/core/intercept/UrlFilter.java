package com.authorization.core.intercept;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class UrlFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.warn("过滤器中的请求路径-getRequestURI->{}", request.getRequestURI());
        log.warn("过滤器中的请求路径-getPathInfo->{}", request.getPathInfo());
        log.warn("过滤器中的请求路径-getContextPath->{}", request.getContextPath());
        log.warn("过滤器中的请求路径-getServletPath->{}", request.getServletPath());

        filterChain.doFilter(request, response);

        log.error("END---过滤器请求执行完成-请求的URL->{}", request.getServletPath());

    }
}
