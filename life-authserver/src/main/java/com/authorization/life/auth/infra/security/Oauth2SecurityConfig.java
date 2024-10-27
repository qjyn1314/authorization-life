package com.authorization.life.auth.infra.security;

import com.authorization.core.security.handle.LoginUrlAuthenticationEntryPoint;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.infra.security.handler.oauth.OAuth2SuccessHandler;
import com.authorization.life.auth.infra.security.service.*;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.security.SecurityCoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
 * <p>
 * /oauth2/authorize >
 * <p>
 * 1. OAuth2AuthorizationEndpointFilter
 * <p>
 * 2. OAuth2AuthorizationCodeAuthenticationConverter
 * 在此处获取当前登录用户的信息-通过 SecurityContextHolder.getContext().getAuthentication();获取到此次会话中的用户信息, 并设置到 OAuth2AuthorizationCodeAuthenticationToken clientPrincipal字段中
 * <p>
 * 3. OAuth2AuthorizationCodeRequestAuthenticationProvider
 * <p>
 * 3.1. RegisteredClientRepository
 * <p>
 * 3.2. OAuth2AuthorizationService  使用redis实现,用于保存当前临时code的信息,过期时间为5分钟, 并创建 OAuth2AuthorizationCodeRequestAuthenticationToken
 * <p>
 * 4. AuthenticationSuccessHandler  -> 自定义的 OAuth2SuccessHandler
 * <p>
 * <p>
 * /oauth2/token >
 * <p>
 * 1. OAuth2TokenEndpointFilter
 * <p>
 * 2. OAuth2AuthorizationCodeAuthenticationConverter
 * 在此处获取当前登录用户的信息-通过 SecurityContextHolder.getContext().getAuthentication(); 获取到此次会话中的用户信息, 并设置到 OAuth2AuthorizationCodeAuthenticationToken 的clientPrincipal 字段中
 * <p>
 * 3.OAuth2AuthorizationCodeAuthenticationProvider
 * <p>
 * 3.1 OAuth2AuthorizationService 使用redis从redis中获取临时code的信息.
 * <p>
 * 3.2 OAuth2TokenCustomizer 自定义accessToken中的内容
 * <p>
 * 3.3 OAuth2AuthorizationService 使用redis保存 code accessToken refrenchToken, 最后设置到 OAuth2AccessTokenAuthenticationToken 中
 * <p>
 * 4.OAuth2AccessTokenResponseAuthenticationSuccessHandler 将token返回
 * <p>
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
                                                                      OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer
    ) throws Exception {
        // 参考: org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer.authorizationEndpoint(endpointConfigurer -> {
            endpointConfigurer
                    // 配置自定义登录成功处理器, 即登录成功之后,在登录页面拼接参数后直接请求此路径, get请求: /oauth2/authorize 的成功处理器,用于重定向到相应的登录成功页面并携带临时code
                    .authorizationResponseHandler(new OAuth2SuccessHandler());
        });

        //配置openid的配置项
//        authorizationServerConfigurer.oidc(Customizer.withDefaults());

//        authorizationServerConfigurer.tokenEndpoint(endpointConfigurer -> {
//            endpointConfigurer
//                    // 配置自定义登录成功处理器, 即登录成功之后, post请求: /oauth2/token 的成功处理器
//                    // 默认使用的是 OAuth2AccessTokenResponseAuthenticationSuccessHandler
//                    .accessTokenResponseHandler();
//        });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 配置请求拦截
        http.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 无需认证即可访问
                        .requestMatchers(SecurityCoreService.IGNORE_PERM_URLS).permitAll()
                        //除以上的请求之外，都需要token
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher));

        //将oauth2.0自定义的配置托管给 SpringSecurity
        http.with(authorizationServerConfigurer, Customizer.withDefaults());
        // 自定义设置accesstoken中JwtToken-Claims中的内容
        http.setSharedObject(OAuth2TokenCustomizer.class, oAuth2TokenCustomizer);

        // 配置 异常处理
        http.exceptionHandling(excHandle -> excHandle
                //当未登录的情况下 自定义该如何跳转。
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint()));
        return http.build();
    }

    /**
     * 自定义accessToken的Claims, 在其中添加自定义字段 token
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(SecurityAuthUserService securityAuthUserService,
                                                                        OauthClientService oauthClientService,
                                                                        RedisUtil redisUtil,
                                                                        HttpServletRequest servletRequest) {
        return new CustomizerOAuth2Token(securityAuthUserService, oauthClientService, redisUtil, servletRequest);
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
     * @param redisTemplate redis操作类
     * @return OAuth2AuthorizationService
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOAuth2AuthorizationService(redisTemplate);
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
                .issuer(SecurityCoreService.DEFAULT_ISSUER)
                .build();
    }


    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        //加载中文认证提示信息
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //加载org/springframework/security包下的中文提示信息 配置文件
        messageSource.setBasename("classpath:messages/messages_zh_CN");
        return messageSource;
    }

}
