//package com.authserver.common.life.security;
//
//import com.authserver.common.life.security.oauth.OAuth2ImplicitAuthenticationConverter;
//import com.authserver.common.life.security.oauth.OAuth2ImplicitAuthenticationProvider;
//import com.authserver.common.life.security.oauth.OAuth2ImplicitSuccessHandler;
//import com.authserver.common.life.security.service.ClientRepository;
//import com.authserver.common.life.security.service.RedisOAuth2AuthorizationConsentService;
//import com.authserver.common.life.security.service.RedisOAuth2AuthorizationService;
//import com.authserver.common.life.security.service.UserDetailCustomizer;
//import com.authserver.common.life.security.util.Jwks;
//import com.authserver.common.life.security.util.OAuth2ConfigurerUtils;
//import com.authserver.common.life.service.OauthClientService;
//import com.authserver.common.life.service.UserService;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
//import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeRequestAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
//import org.springframework.security.web.util.matcher.RequestMatcher;
//
//import java.util.Arrays;
//
///**
// * Oauth2 + security 配置类
// */
//@Configuration(proxyBeanMethods = false)
//public class Oauth2SecurityConfig {
//
//    @Autowired
//    private RedisTemplate<String, String> jdkHashValueRedisTemplate;
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
//                                                                      OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer,
//                                                                      RegisteredClientRepository registeredClientRepository) throws Exception {
//        // 配置oauth2 configurer
//        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
//                new OAuth2AuthorizationServerConfigurer<>();
//
//        authorizationServerConfigurer.authorizationEndpoint(oAuth2TokenEndpointConfigurer ->
//                oAuth2TokenEndpointConfigurer.authorizationRequestConverter(new DelegatingAuthenticationConverter(Arrays.asList(
//                                new OAuth2ImplicitAuthenticationConverter(),
//                                new OAuth2AuthorizationCodeRequestAuthenticationConverter())))
//                        .authorizationResponseHandler(new OAuth2ImplicitSuccessHandler())
//                        .authenticationProvider(createImplicitProvider(http, registeredClientRepository))
//                        .authenticationProvider(createOAuth2AuthorizationCodeRequestAuthenticationProvider(http)));
//        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
//
//        // 将oauth2配置 托管给  httpSecurity，即 HttpSecurity 增加Oauth2的相关配置
//        http.requestMatcher(endpointsMatcher)
//                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
//                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
//                .apply(authorizationServerConfigurer);
//
//        http.setSharedObject(OAuth2TokenCustomizer.class, oAuth2TokenCustomizer);
//        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(SecurityContant.SSO_LOGIN));
//        return http.build();
//    }
//
//    /**
//     * 创建简化模式provider
//     *
//     * @param http                       HttpSecurity
//     * @param registeredClientRepository client repository
//     * @return OAuth2ImplicitAuthenticationProvider
//     */
//    private OAuth2ImplicitAuthenticationProvider createImplicitProvider(HttpSecurity http,
//                                                                        RegisteredClientRepository registeredClientRepository) {
//        JwtEncoder jwtEncoder = OAuth2ConfigurerUtils.getJwtEncoder(http);
//        OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = OAuth2ConfigurerUtils.getJwtCustomizer(http);
//        OAuth2ImplicitAuthenticationProvider oAuth2ImplicitAuthenticationProvider =
//                new OAuth2ImplicitAuthenticationProvider(OAuth2ConfigurerUtils.getAuthorizationService(http), jwtEncoder, registeredClientRepository);
//        if (jwtCustomizer != null) {
//            oAuth2ImplicitAuthenticationProvider.setJwtCustomizer(jwtCustomizer);
//        }
//        return oAuth2ImplicitAuthenticationProvider;
//    }
//
//    private OAuth2AuthorizationCodeRequestAuthenticationProvider createOAuth2AuthorizationCodeRequestAuthenticationProvider(HttpSecurity http) {
//
//        return new OAuth2AuthorizationCodeRequestAuthenticationProvider(
//                OAuth2ConfigurerUtils.getRegisteredClientRepository(http),
//                OAuth2ConfigurerUtils.getAuthorizationService(http),
//                OAuth2ConfigurerUtils.getAuthorizationConsentService(http));
//    }
//
//    @Bean
//    public RegisteredClientRepository registeredClientRepository(OauthClientService clientService) {
//        return new ClientRepository(clientService);
//    }
//
//    @Bean
//    public OAuth2AuthorizationService authorizationService() {
//        return new RedisOAuth2AuthorizationService(jdkHashValueRedisTemplate);
//    }
//
//    @Bean
//    public OAuth2AuthorizationConsentService authorizationConsentService() {
//        return new RedisOAuth2AuthorizationConsentService(jdkHashValueRedisTemplate);
//    }
//
//    @Bean
//    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(UserService userDetailService,
//                                                                           RedisTemplate<String, Object> redisHelper) {
//        return new UserDetailCustomizer(userDetailService, redisHelper);
//    }
//
//    @Bean
//    public JWKSource<SecurityContext> jwkSource() {
//        RSAKey rsaKey = Jwks.generateRsa();
//        JWKSet jwkSet = new JWKSet(rsaKey);
//        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//    }
//
//    @Bean
//    public ProviderSettings providerSettings() {
//        return ProviderSettings.builder()
//                .issuer(SecurityContant.ISSUER)
//                .build();
//    }
//
//}
