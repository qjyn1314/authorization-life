package com.authorization.life.security.service;


import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 将client-id设置到请求头中
 */
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        // 添加一个自定义头
        context.getHeaders().header("client-id", context.getRegisteredClient().getClientId());
    }
}
