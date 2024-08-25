package com.authorization.gateway.filter;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.gateway.execption.UnauthorizedException;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SecurityCoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * jwt转换过滤器，将token转换为jwtToken
 */
@Slf4j
@Component
public class JwtTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements InitializingBean {

    public static final String JWT_TOKEN = "JwtToken";

    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = getToken(request);
            log.info("accessToken中的token字符串是->{}", token);
            String realJwtToken = getByToken(token);
            ServerWebExchange jwtExchange = exchange.mutate().request(request.mutate()
                    // 下传Header信息,存储了用户信息的自定义JwtToken
                    .header(SecurityCoreService.AUTH_POSITION, realJwtToken)
                    .build()).build();
            return chain.filter(jwtExchange);
        };
    }

    private String getByToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        String jwtToken = jwtService.createJwtToken(JSONUtil.toBean(redisUtil.get(SecurityCoreService.getUserTokenKey(token)), LinkedHashMap.class));
        log.info("网关下传到其他服务的JwtToken是-{}", jwtToken);
        return jwtToken;
    }

    /**
     * 获取jwtToken，并解析获取其中的token信息
     *
     * @param request request
     * @return token
     */
    private String getToken(ServerHttpRequest request) {
        String accessToken = null;
        // 先检查header中有没有accessToken
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isNotBlank(authorization) && StrUtil.startWithIgnoreCase(authorization, SecurityCoreService.ACCESS_TOKEN_TYPE)) {
            accessToken = StrUtil.removePrefixIgnoreCase(authorization, SecurityCoreService.ACCESS_TOKEN_TYPE).trim();
        }
        // 如果header中没有，则检查url参数并赋值
        if (StrUtil.isBlank(accessToken)) {
            accessToken = Optional.of(request.getQueryParams()).map(param -> param.getFirst(SecurityCoreService.ACCESS_TOKEN)).orElse(null);
        }
        // 如果url参数中也没有赋值, 则检查cookie并赋值
        if (StrUtil.isBlank(accessToken)) {
            MultiValueMap<String, HttpCookie> cookiesMap = request.getCookies();
            if (CollUtil.isEmpty(cookiesMap)) {
                return null;
            }
            accessToken = Optional.ofNullable(cookiesMap.getFirst(SecurityCoreService.ACCESS_TOKEN)).orElse(new HttpCookie(SecurityCoreService.ACCESS_TOKEN, "")).getValue();
        }
        log.info("网关接受到前端的AccessToken是->{}", accessToken);
        if (StrUtil.isBlank(accessToken)) {
            // 此处请求直接放过, 请求路径的白名单, 将交给 core包里面的 JwtAuthenticationFilter
            return null;
        }
        Jwt decode = null;
        try {
            // 当accessToken不为空时做校验, 并解析
            decode = jwtDecoder.decode(accessToken);
            return Optional.ofNullable(decode.getClaims()).orElse(Map.of()).getOrDefault(SecurityCoreService.CLAIM_TOKEN_KEY, "").toString();
        } catch (Exception e) {
            log.error("解析Token失败...", e);
            // 解析失败则抛出异常
            throw new UnauthorizedException();
        }
    }

    @Override
    public String name() {
        return JWT_TOKEN;
    }
}
