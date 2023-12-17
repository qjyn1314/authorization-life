package com.authorization.life.auth.infra.security;

import com.authorization.core.security.handle.LoginUrlAuthenticationEntryPoint;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.infra.security.handler.oauth.OAuth2SuccessHandler;
import com.authorization.life.auth.infra.security.service.*;
import com.authorization.life.auth.infra.security.util.Jwks;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.security.SecurityConstant;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
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
 * <p>
 * 官网:
 * <p>
 * https://docs.spring.io/spring-security/reference/getting-spring-security.html
 *
 * @author wangjunming
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
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

//        authorizationServerConfigurer
//                .authorizationEndpoint(endpointConfigurer -> {
//                    //参考：https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html
//                    endpointConfigurer
//                            // 配置自定义的请求成功的处理器,
//                            // 注意 如果 TokenSettings.accessTokenFormat 设置为 OAuth2TokenFormat.REFERENCE 则需要设置此handle.
//                            // 此时将不会进入到 自定义的token设置类中, 即 CustomizerOAuth2Token, token中没有自定义的uuid 存储到 redis中 ,gateway中将获取不到此登录用户的信息.
//                            // 这个时候只能在 auth-life 服务中获取到当前登录用户的信息, 不再是前后端分离的分布式登录流程.
//                            .authorizationResponseHandler(new OAuth2SuccessHandler());
//                });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 配置请求拦截
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        // 无需认证即可访问
                        .requestMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                        //除以上的请求之外，都需要token
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                //配置formLogin
                .formLogin(Customizer.withDefaults());

//        http.apply(authorizationServerConfigurer);

//        http.apply(authServerConfig->)
//                //将oauth2.0的配置托管给 SpringSecurity
//                .apply(authorizationServerConfigurer);

        // 自定义设置accesstoken为jwt形式
        http.setSharedObject(OAuth2TokenCustomizer.class, oAuth2TokenCustomizer);
        // 配置 异常处理
        http
                .exceptionHandling(excHandle -> excHandle
                        //当未登录的情况下 该如何跳转。
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint()));

        return http.build();
    }

    /**
     * 自定义accessToken的实现为 jwtToken
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(SecurityAuthUserService securityAuthUserService,
                                                                        OauthClientService oauthClientService,
                                                                        StringRedisService stringRedisService,
                                                                        HttpServletRequest servletRequest) {
        return new CustomizerOAuth2Token(securityAuthUserService, oauthClientService, stringRedisService, servletRequest);
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
     * @param redisTemplate      redis操作类
     * @param stringRedisService redis字符串的操作类
     * @return OAuth2AuthorizationService
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate,
                                                           StringRedisService stringRedisService) {
        return new RedisOAuth2AuthorizationService(redisTemplate, stringRedisService);
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
    public AuthorizationServerSettings providerSettings() {
        //此处为oauth授权服务的发行者，即此授权服务地址
        return AuthorizationServerSettings.builder()
                //发布者的url地址,一般是本系统访问的根路径
                .issuer(SecurityConstant.ISSUER)
                .build();
    }

}
