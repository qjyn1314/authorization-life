package com.authorization.core.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.entity.UserDetail;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.jwt.Jwts;
import com.authorization.utils.security.SsoSecurityProperties;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 每一次请求将从gateway中获取前端的token，gateway解析为每一个服务所使用的JWT-token请求头中获取token，并解析为当前登录用户信息。
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter implements InitializingBean {

    private JWSVerifier verifier;
    private final SsoSecurityProperties ssoSecurityProperties;

    public JwtAuthenticationFilter(SsoSecurityProperties ssoSecurityProperties) {
        this.ssoSecurityProperties = ssoSecurityProperties;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String secret = ssoSecurityProperties.getSecret();
        log.info("jwt密钥是：{}", secret);
        verifier = Jwts.verifier(secret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("请求路径是-{}", JSONUtil.toJsonStr(request.getRequestURI()));
        String jwt = request.getHeader(Jwts.HEADER_JWT);
        log.info("进入到-JwtAuthenticationFilter-过滤器-jwtToken-{}", jwt);

        Boolean authEnable = ssoSecurityProperties.getEnable();
        if (!authEnable && StrUtil.isBlank(jwt)) {
            //如果配置中不需要登录, 则设置访客用户为当前登录用户
            handleAnonymousUser(request, response, chain);
            return;
        }

        if (StrUtil.isBlank(jwt)) {
            chain.doFilter(request, response);
            return;
        }
        JWSObject jwsObject = Jwts.parse(jwt);
        if (!Jwts.verify(jwsObject, verifier)) {
            log.error("Jwt verify failed! JWT: [{}]", jwt);
            chain.doFilter(request, response);
            return;
        }
        // 如果此处的jwt信息解析不出来, 则设置访客用户为当前登录用户信息.
        UserDetail userDetail = jwsObject.getPayload().toType(payload -> StrUtil.isBlank(payload.toString()) ?
                UserDetail.anonymous() : JsonHelper.readValue(payload.toString(), UserDetail.class));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private void handleAnonymousUser(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(UserDetail.anonymous(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }
}
