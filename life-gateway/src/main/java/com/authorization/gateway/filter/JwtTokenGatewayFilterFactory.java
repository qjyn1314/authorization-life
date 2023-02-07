package com.authorization.gateway.filter;


import cn.hutool.core.util.StrUtil;
import com.authorization.gateway.entity.RequestContext;
import com.authorization.gateway.entity.UserDetail;
import com.authorization.gateway.execption.UnauthorizedException;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.json.JsonHelper;
import com.authorization.utils.jwt.Jwts;
import com.authorization.utils.security.SecurityConstant;
import com.authorization.utils.security.SsoSecurityProperties;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
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
    private JWSSigner signer;
    private final JWSHeader jwsHeader = Jwts.header();

    @Autowired
    private StringRedisService stringRedisService;

    @Override
    public void afterPropertiesSet() {
        String secret = ssoSecurityProperties.getSecret();
        signer = Jwts.signer(secret);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = getToken(request);
            // 获取当前用户的信息
            String userDetailStr = StrUtil.isBlank(token) ? null : stringRedisService.strGet(SecurityConstant.getUserTokenKey(token));
            // 若jwt不存在，则封入一个空字符串，到权限拦截器处理。因为有些api是不需要登录的，故在此不处理。
            UserDetail userDetail = StrUtil.isNotBlank(userDetailStr) ? JsonHelper.readValue(userDetailStr, UserDetail.class) : null;
            userDetailStr = Optional.ofNullable(userDetailStr).orElse(StrUtil.EMPTY);
            // 创建JWS对象
            JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(userDetailStr));
            // 签名并序列化转换为真正存储用户信息的jwtToken
            String jwtToken = Jwts.signAndSerialize(jwsObject, signer);
            log.info("网关下传到其他服务的JwtToken是-{}", jwtToken);
            ServerWebExchange jwtExchange = exchange.mutate().request(request.mutate().header(Jwts.HEADER_JWT, jwtToken).build()).build();
            return chain.filter(jwtExchange).contextWrite(ctx -> ctx.put(RequestContext.CTX_KEY, ctx.<RequestContext>getOrEmpty(RequestContext.CTX_KEY).orElse(new RequestContext()).setUserDetail(userDetail)));
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
        if (StrUtil.startWithIgnoreCase(authorization, SecurityConstant.Header.TYPE_BEARER)) {
            accessToken = StrUtil.removePrefixIgnoreCase(authorization, SecurityConstant.Header.TYPE_BEARER).trim();
        }
        // 如果header中没有，则检查url参数并赋值
        if (StrUtil.isBlank(accessToken)) {
            accessToken = Optional.of(request.getQueryParams()).map(param -> param.getFirst(SecurityConstant.ACCESS_TOKEN)).orElse(null);
        }
        if (StrUtil.isBlank(accessToken)) {
            return null;
        }
        Map<String, Object> map = Jwts.parse(accessToken).getPayload().toJSONObject();
        String token = (String) map.get(SecurityConstant.CLAIM_TOKEN_KEY);
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
