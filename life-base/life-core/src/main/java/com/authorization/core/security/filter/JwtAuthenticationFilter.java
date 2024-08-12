package com.authorization.core.security.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.exception.handle.DefaultErrorMsg;
import com.authorization.core.security.exception.NotLoggedException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Boolean authEnable = getAuthEnable(request);
        log.info("authEnable -> {}", authEnable);
        if (authEnable) {
            handleLoginUser(request, response, chain, jwtToken);
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
            Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(item -> item.getName().equals(SsoSecurityProperties.ACCESS_TOKEN)).findFirst();
            if (optionalCookie.isPresent()) {
                jwt = optionalCookie.get().getValue();
            }
        }
        return jwt;
    }

    /**
     * 判断是否需要登录.
     *
     * @param request 当前请求信息
     * @return Boolean true- 必须有登录用户信息, false- 不需要登录用户的信息, 为默认用户信息.
     */
    private Boolean getAuthEnable(HttpServletRequest request) {
        //不需要认证则 直接返回false,
        return ssoSecurityProperties.getEnable();
    }

    /**
     * 设置当前登录用户
     *
     * @param request
     * @param response
     * @param chain
     * @param jwt
     */
    private void handleLoginUser(HttpServletRequest request, HttpServletResponse response, FilterChain chain, String jwt) {
        // 如果此处的jwt信息解析不出来, 则设置访客用户为当前登录用户信息.
        UserDetail userDetail = getUserDetailByInteriorJwt(jwt);
        Assert.notNull(userDetail, () -> new NotLoggedException(DefaultErrorMsg.EXPIRED_STRATEGY_MSG.getMsgCode()));
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public UserDetail getUserDetailByInteriorJwt(String interiorJwt) {
        Map<String, Object> fromJwtToken = jwtService.getClaimsFromJwtToken(interiorJwt);
        return CollUtil.isNotEmpty(fromJwtToken) ? BeanUtil.toBean(fromJwtToken, UserDetail.class) : null;
    }
}
