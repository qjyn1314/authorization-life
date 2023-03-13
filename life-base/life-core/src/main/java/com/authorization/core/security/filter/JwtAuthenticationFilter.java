package com.authorization.core.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.core.security.entity.UserDetail;
import com.authorization.core.security.exception.NotLoggedException;
import com.authorization.core.exception.handle.DefaultErrorMsg;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.jwt.Jwts;
import com.authorization.utils.security.SsoSecurityProperties;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

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
        // 开启登录且请求路径不在忽略认证的url集合中
        Boolean authEnable = getAuthEnable(request);
        log.info("authEnable -> {}", authEnable);
        if (authEnable) {
            UserDetail userDetail = handleLoginUser(request, response, chain, jwt);
            if (Objects.isNull(userDetail)) {
                SecurityContextHolder.clearContext();
                throw new NotLoggedException(DefaultErrorMsg.USER_NOT_LOGIN.getMsgCode());
            }
            log.info("登录成功之后解析jwt后的用户信息是-{}", JsonHelper.toJson(userDetail));
            chain.doFilter(request, response);
        } else {
            //如果有 jwt, 则进行设置当前登录用户信息.
            if (StrUtil.isNotBlank(jwt)) {
                UserDetail userDetail = handleLoginUser(request, response, chain, jwt);
                //如果给到的jwt不能解析出用户信息则设置匿名用户
                if (Objects.isNull(userDetail)) {
                    handleAnonymousUser(request, response, chain);
                }
            } else {
                //如果配置中不需要登录, 则设置访客用户为当前登录用户
                handleAnonymousUser(request, response, chain);
            }
            chain.doFilter(request, response);
        }
    }

    /**
     * 判断是否需要登录.
     *
     * @param request 当前请求信息
     * @return Boolean true- 必须有登录用户信息, false- 不需要登录用户的信息, 为默认用户信息.
     */
    private Boolean getAuthEnable(HttpServletRequest request) {
        Boolean enable = ssoSecurityProperties.getEnable();
        //不需要认证则 直接返回false,
        if (!enable) {
            return false;
        }
        String requestUrl = request.getRequestURI();
        if (!StringUtils.hasText(requestUrl)) {
            return true;
        }
        //需要验证, 但是又匹配到了 不需要验证的url, 则匹配通过后 返回false
        Set<String> ignorePermUrls = ssoSecurityProperties.getIgnorePermUrls();
        for (String ignorePermUrl : ignorePermUrls) {
            // 验证当前请求, 是否符合 指定的url路径.
            boolean matches = AntPathRequestMatcher.antMatcher(ignorePermUrl).matches(request);
            if (matches) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置当前登录用户
     *
     * @param request
     * @param response
     * @param chain
     * @param jwt
     */
    private UserDetail handleLoginUser(HttpServletRequest request, HttpServletResponse response, FilterChain chain, String jwt) {
        // 如果此处的jwt信息解析不出来, 则设置访客用户为当前登录用户信息.
        UserDetail userDetail = getUserDetailByInteriorJwt(jwt);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return userDetail;
    }

    public UserDetail getUserDetailByInteriorJwt(String interiorJwt) {
        if (StrUtil.isBlank(interiorJwt)) {
            return null;
        }
        JWSObject jwsObject = Jwts.parse(interiorJwt);
        if (!Jwts.verify(jwsObject, verifier)) {
            log.error("Jwt verify failed! JWT: [{}]", interiorJwt);
            return null;
        }
        if (StrUtil.isBlank(jwsObject.getPayload().toString())) {
            log.error("Jwt token fail! no user info");
            return null;
        }
        // 如果此处的jwt信息解析不出来, 则设置访客用户为当前登录用户信息.
        return jwsObject.getPayload().toType(payload -> StrUtil.isBlank(payload.toString()) ?
                UserDetail.anonymous() : JsonHelper.readValue(payload.toString(), UserDetail.class));
    }

    /**
     * 设置匿名用户
     */
    private void handleAnonymousUser(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(UserDetail.anonymous(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
