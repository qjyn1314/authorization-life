package com.authserver.common.life.security;

import com.authserver.common.life.security.handler.password.PasswordSuccessHandle;
import com.authserver.common.life.security.service.RedisOAuth2AuthorizationConsentService;
import com.authserver.common.life.security.service.RedisOAuth2AuthorizationService;
import com.authserver.common.life.security.service.RegisteredClientService;
import com.authserver.common.life.security.util.Jwks;
import com.authserver.common.life.service.OauthClientService;
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
        //OAuth2 配置信息
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();


        authorizationServerConfigurer.authorizationEndpoint(oAuth2AuthorizationEndpointConfigurer ->

                        oAuth2AuthorizationEndpointConfigurer

                                .authorizationResponseHandler(new PasswordSuccessHandle())

                //添加
//                        .authenticationProvider()

        );


        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http.requestMatcher(endpointsMatcher)

                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())

                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))

                .apply(authorizationServerConfigurer);

        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint());

//        http.formLogin(Customizer.withDefaults());
        http.formLogin();
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
