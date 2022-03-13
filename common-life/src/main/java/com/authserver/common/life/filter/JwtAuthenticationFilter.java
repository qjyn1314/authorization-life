package com.authserver.common.life.filter;

import cn.hutool.core.util.StrUtil;
import com.authserver.common.life.entity.UserDetail;
import com.authserver.common.life.json.JsonHelper;
import com.authserver.common.life.jwt.Jwts;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
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

    @Value(Jwts.SECRET_EXPRESS)
    private String secret;
    private JWSVerifier verifier;

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        verifier = Jwts.verifier(secret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwt = request.getHeader(Jwts.HEADER_JWT);
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
        UserDetail userDetail = jwsObject.getPayload().toType(payload -> StrUtil.isBlank(payload.toString()) ?
                UserDetail.anonymous() : JsonHelper.readValue(payload.toString(), UserDetail.class));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);

    }
}