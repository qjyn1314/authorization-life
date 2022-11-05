package com.authorization.life.security;

import com.authorization.core.security.SecurityConstant;
import com.authorization.core.security.handle.LoginUrlAuthenticationEntryPoint;
import com.authorization.life.security.handler.oauth.OAuth2SuccessHandler;
import com.authorization.life.security.service.*;
import com.authorization.life.security.util.Jwks;
import com.authorization.life.service.OauthClientService;
import com.authorization.redis.start.util.StrRedisHelper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * 整合 oauth2_authorization 的配置类。
 * <p>
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
@Slf4j
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class Oauth2SecurityConfig {

    /**
     * oauth2.0配置，需要托管给 HttpSecurity
     * <p>
     * 详解： https://blog.51cto.com/u_14558366/5605065
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer) throws Exception {

        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        authorizationServerConfigurer
                .authorizationEndpoint(endpointConfigurer -> {
                    //参考：https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html
                    endpointConfigurer
                            //配置自定义的请求成功的处理器
                            .authorizationResponseHandler(new OAuth2SuccessHandler());
                });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 配置请求拦截
        http.requestMatcher(endpointsMatcher)

                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        // 无需认证即可访问
                        .antMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                        //除以上的请求之外，都需要accessToken 
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                //将oauth2.0的配置托管给 SpringSecurity
                .apply(authorizationServerConfigurer);

        // 设置accesstoken为jwt形式
        http.setSharedObject(OAuth2TokenCustomizer.class, oAuth2TokenCustomizer);

        http.formLogin(Customizer.withDefaults());
        // 配置 异常处理
        http
                .exceptionHandling()
                //当未登录的情况下 该如何跳转。
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint());

        return http.build();
    }

    /**
     * 自定义accessToken的实现为 jwtToken
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(SecurityAuthUserService securityAuthUserService,
                                                                        OauthClientService oauthClientService,
                                                                        StrRedisHelper strRedisHelper) {
        return new CustomizerOAuth2Token(securityAuthUserService,oauthClientService,strRedisHelper);
    }

    /**
     * JWT的加密算法，说明：https://www.rfc-editor.org/rfc/rfc7515
     *
     * @return JWKSource
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }


    /**
     * 注册client
     *
     * @param clientService 自定义的client端信息
     * @return RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(OauthClientService clientService) {
        return new RegisteredClientService(clientService);
    }

    /**
     * 保存授权信息，授权服务器给我们颁发来token，那我们肯定需要保存吧，由这个服务来保存
     *
     * @param redisTemplate  redis操作类
     * @param strRedisHelper redis字符串的操作类
     * @return OAuth2AuthorizationService
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate,
                                                           StrRedisHelper strRedisHelper) {
        return new RedisOAuth2AuthorizationService(redisTemplate, strRedisHelper);
    }

    /**
     * 如果是授权码的流程，可能客户端申请了多个权限，比如：获取用户信息，修改用户信息，此Service处理的是用户给这个客户端哪些权限，比如只给获取用户信息的权限
     *
     * @param redisTemplate redis操作类
     * @return OAuth2AuthorizationConsentService
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationConsentService(redisTemplate);
    }

    /**
     * AccessToken的提供者
     *
     * @return ProviderSettings
     */
    @Bean
    public ProviderSettings providerSettings() {
        //此处为oauth授权服务的发行者，即此授权服务地址
        return ProviderSettings.builder()
                //发布者的url地址,一般是本系统访问的根路径
                .issuer(SecurityConstant.ISSUER)
                //授权端点
                .authorizationEndpoint("/oauth2/authorize")
                // 配置获取token的端点路径
                .tokenEndpoint("/oauth2/token")
                //令牌自省端点
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                //令牌撤销端点
                .tokenRevocationEndpoint("/oauth2/revoke")
                //jwk 设置端点
                .jwkSetEndpoint("/oauth2/jwks")
                //oidc 用户信息端点
                .oidcUserInfoEndpoint("/userinfo")
                //oidc 客户端注册端点
                .oidcClientRegistrationEndpoint("/connect/register")
                .build();
    }

}
