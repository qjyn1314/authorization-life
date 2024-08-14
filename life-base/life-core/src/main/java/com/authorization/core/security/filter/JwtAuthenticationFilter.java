package com.authorization.core.security.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.security.entity.UserHelper;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SsoSecurityProperties;
import com.authorization.utils.security.UserDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * 每一次请求将从gateway中获取前端的token，gateway解析为每一个服务所使用的JWT-token请求头中获取token，并解析为当前登录用户信息。
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter implements InitializingBean {

    private final SsoSecurityProperties ssoSecurityProperties;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(SsoSecurityProperties ssoSecurityProperties, JwtService jwtService) {
        this.ssoSecurityProperties = ssoSecurityProperties;
        this.jwtService = jwtService;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("请求路径是-{}", JSONUtil.toJsonStr(request.getRequestURI()));
        String jwtToken = getJwtToken(request);
        log.info("进入到-JwtAuthenticationFilter-过滤器-jwtToken-{}", jwtToken);
        if (StrUtil.isNotBlank(jwtToken)) {
            UserHelper.setUserDetail(getUserDetailByInteriorJwt(jwtToken));
        }

        chain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        // 从gateway中设置的请求头获取
        String jwt = request.getHeader(SsoSecurityProperties.AUTH_POSITION);
        if (StrUtil.isBlank(jwt)) {
            // 从请求头中获取
            jwt = request.getHeader(SsoSecurityProperties.ACCESS_TOKEN);
        }
        if (StrUtil.isBlank(jwt)) {
            // 从cookie中获取
            Optional<Cookie> optionalCookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                    .filter(item -> item.getName().equals(SsoSecurityProperties.ACCESS_TOKEN)).findFirst();
            if (optionalCookie.isPresent()) {
                jwt = optionalCookie.get().getValue();
            }
        }
        return jwt;
    }

    public UserDetail getUserDetailByInteriorJwt(String interiorJwt) {
        Map<String, Object> fromJwtToken = jwtService.getClaimsFromJwtToken(interiorJwt);
        return CollUtil.isNotEmpty(fromJwtToken) ? BeanUtil.toBean(fromJwtToken, UserDetail.class) : null;
    }
}
