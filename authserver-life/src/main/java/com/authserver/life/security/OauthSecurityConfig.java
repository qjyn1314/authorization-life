package com.authserver.life.security;

import com.authserver.common.security.LoginUrlAuthenticationEntryPoint;
import com.authserver.life.security.handler.password.PasswordSuccessHandle;
import com.authserver.life.security.service.RedisOAuth2AuthorizationConsentService;
import com.authserver.life.security.service.RedisOAuth2AuthorizationService;
import com.authserver.life.security.service.RegisteredClientService;
import com.authserver.life.security.util.Jwks;
import com.authserver.life.service.OauthClientService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * 相关教程说明
 * <p>
 * https://book-spring-security-reference.vnzmi.com/3.2_httpsecurity.html
 * <p>
 * https://spring.io/projects/spring-security/
 * <p>
 * https://blog.csdn.net/sinat_29899265/article/details/80736498
 * <p>
 * https://www.hangge.com/blog/cache/detail_2680.html
 * <p>
 * oauth2 security 的配置信息，关键是将配置信息托管给 HttpSecurity
 * <p>
 * https://juejin.cn/post/6985411823144615972
 */
@Configuration(proxyBeanMethods = false)
public class OauthSecurityConfig {

    /**
     * 将oauth的配置交给 HttpSecurity
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        //OAuth2 配置类信息
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authServerConfig = new OAuth2AuthorizationServerConfigurer<>();

        authServerConfig
                //配置授权
                .authorizationEndpoint(endpointConfigurer ->
                        endpointConfigurer
                                //配置请求成功的处理类
                                .authorizationResponseHandler(new PasswordSuccessHandle())
                                //client认证授权，处理类
//                                .authenticationProvider()
                );

        //路径匹配器
        RequestMatcher matcher = authServerConfig.getEndpointsMatcher();

        http.requestMatcher(matcher)
                //所有的请求都需要认证
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                // 跨站请求伪造 ，参考：https://www.jianshu.com/p/e825e67fcf28
                .csrf(csrf -> csrf.ignoringRequestMatchers(matcher))
                //将oauth2.0的配置托管给HttpSecurity
                .apply(authServerConfig);
        // 配置 异常处理
        http.exceptionHandling()
                //当未登录的情况下 该如何跳转。
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint());
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));

//        http.formLogin(Customizer.withDefaults());
//        http.formLogin();
        return http.build();
    }


    @Bean
    public RegisteredClientRepository registeredClientRepository(OauthClientService clientService) {
        return new RegisteredClientService(clientService);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, String> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RedisTemplate<String, String> redisTemplate) {
        return new RedisOAuth2AuthorizationConsentService(redisTemplate);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().issuer(SecurityContant.ISSUER).build();
    }

}
