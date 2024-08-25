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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

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
        UserHelper.setUserDetail(getUserDetailByInteriorJwt(jwtToken));
        // 此处如果是需要放过的请求路径, 将请求路径的校验交给SpringSecurity进行校验
        chain.doFilter(request, response);
    }

    private String getJwtToken(HttpServletRequest request) {
        // 从gateway中设置的请求头获取
        return request.getHeader(SsoSecurityProperties.AUTH_POSITION);
    }

    public UserDetail getUserDetailByInteriorJwt(String interiorJwt) {
        if (StrUtil.isBlank(interiorJwt)) {
            return UserDetail.anonymous();
        }
        Map<String, Object> fromJwtToken = jwtService.getClaimsFromJwtToken(interiorJwt);
        return CollUtil.isNotEmpty(fromJwtToken) ? BeanUtil.toBean(fromJwtToken, UserDetail.class) : null;
    }

}
