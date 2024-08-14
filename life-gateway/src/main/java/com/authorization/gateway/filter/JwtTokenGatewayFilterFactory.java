package com.authorization.gateway.filter;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.authorization.gateway.entity.RequestContext;
import com.authorization.gateway.execption.UnauthorizedException;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.SsoSecurityProperties;
import com.authorization.utils.security.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

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
    private SsoSecurityProperties ssoSecurityProperties;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void afterPropertiesSet() {
        String secret = ssoSecurityProperties.getSecret();

    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = getToken(request);
            // 获取当前用户的信息
            UserDetail userDetail = null;
            if (StrUtil.isNotBlank(token)) {
                userDetail = JSONUtil.toBean(Optional.ofNullable(redisUtil.get(SecurityCoreService.getUserTokenKey(token))).orElse(""), UserDetail.class);
                String jwtToken = jwtService.createJwtToken(JSONUtil.toBean(JSONUtil.toJsonStr(userDetail), Map.class));
                log.info("网关下传到其他服务的JwtToken是-{}", jwtToken);
            }
            ServerWebExchange jwtExchange = exchange.mutate().request(request.mutate()
//                    .header(SsoSecurityProperties.ACCESS_TOKEN, null)
                    .build()).build();
            return chain.filter(jwtExchange).contextWrite(ctx ->
                    ctx.put(RequestContext.CTX_KEY, ctx.<RequestContext>getOrEmpty(RequestContext.CTX_KEY)
                            .orElse(new RequestContext()).setUserDetail(null)));
        };
    }

    /**
     * 获取jwtToken，并解析获取其中的token信息
     *
     * @param request request
     * @return token
     */
    private String getToken(ServerHttpRequest request) {
        String authorization = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).orElse(null);
        String accessToken = null;
        // 先检查header中有没有accessToken
//        if (StrUtil.startWithIgnoreCase(authorization, SecurityCoreService.Header.TYPE_BEARER)) {
//            accessToken = StrUtil.removePrefixIgnoreCase(authorization, SecurityCoreService.Header.TYPE_BEARER).trim();
//        }
        // 如果header中没有，则检查url参数并赋值
        if (StrUtil.isBlank(accessToken)) {
            accessToken = Optional.of(request.getQueryParams()).map(param -> param.getFirst(SecurityCoreService.ACCESS_TOKEN)).orElse(null);
        }
        if (StrUtil.isBlank(accessToken)) {
            return null;
        }
        String token = Optional.ofNullable(jwtService.getClaimsFromJwtToken(accessToken)).orElse(Map.of()).getOrDefault(SecurityCoreService.CLAIM_TOKEN_KEY, "").toString();
        if (StrUtil.isBlank(token)) {
            // 若有jwt但没有token，则jwt一定有问题
            throw new UnauthorizedException();
        }
        return token;
    }

    @Override
    public String name() {
        return JWT_TOKEN;
    }
}
