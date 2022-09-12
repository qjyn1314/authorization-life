package com.authorization.life.security;

import com.authorization.core.filter.JwtAuthenticationFilter;
import com.authorization.core.security.SecurityConstant;
import com.authorization.core.security.UserDetailService;
import com.authorization.core.security.config.SsoSecurityProperties;
import com.authorization.core.security.handle.TokenInformationExpiredStrategy;
import com.authorization.life.security.handler.sso.SsoFailureHandler;
import com.authorization.life.security.handler.sso.SsoLogoutHandle;
import com.authorization.life.security.handler.sso.SsoSuccessHandler;
import com.authorization.life.security.sso.CaptchaAuthenticationDetailsSource;
import com.authorization.life.security.sso.UsernamePasswordAuthenticationProvider;
import com.authorization.life.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 默认的Security配置信息
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class DefaultSecurityConfig {

    @Autowired
    private RedisTemplate<String, Object> redisHelper;
    @Autowired
    private UserDetailService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private OAuth2AuthorizationService authorizationService;
    @Autowired
    private RegisteredClientRepository registeredClientService;
    @Autowired
    private SsoSecurityProperties ssoSecurityProperties;

    /**
     * 默认的过滤链信息
     *
     * @param http                        Security主配置
     * @param authenticationDetailsSource 错误次数验证码的信息
     * @param jwtAuthenticationFilter     jwt Token过滤器
     * @param usernamePasswordProvider    用户名密码验证类
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          CaptchaAuthenticationDetailsSource authenticationDetailsSource,
                                                          JwtAuthenticationFilter jwtAuthenticationFilter,
                                                          AuthenticationProvider usernamePasswordProvider)
            throws Exception {
        http
                // 前后端分离工程需要 禁用csrf-取消csrf防护-参考：https://blog.csdn.net/yjclsx/article/details/80349906
                .csrf().disable()
                .sessionManagement()
                //限制同一账号只能一个用户使用
                .maximumSessions(1)
                //会话过期后的配置
                .expiredSessionStrategy(new TokenInformationExpiredStrategy());
                /*
                Spring Security下的枚举SessionCreationPolicy,管理session的创建策略
                ALWAYS
                    总是创建HttpSession
                IF_REQUIRED
                    Spring Security只会在需要时创建一个HttpSession
                NEVER
                    Spring Security不会创建HttpSession，但如果它已经存在，将可以使用HttpSession
                STATELESS
                    Spring Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
                 */
        // 使用session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 设置无需认证的路径
        http.authorizeRequests()
                // 无需认证即可访问
                .antMatchers("/oauth2/**").permitAll()
                .antMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 配置退出登录配置
        http.logout()
                .logoutUrl(SecurityConstant.SSO_LOGOUT)
                .addLogoutHandler(new SsoLogoutHandle(authorizationService, redisHelper))
                //在此处可以删除相应的cookie
                .deleteCookies(SecurityConstant.JSESSIONID)
                .invalidateHttpSession(true)
                .clearAuthentication(true);
        // 配置表单登录配置
        http.formLogin()
                .loginProcessingUrl(SecurityConstant.SSO_LOGIN)
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(new SsoSuccessHandler())
                .failureHandler(new SsoFailureHandler());
        // 添加jwtfilter  过滤器顺序为 jwtAuthenticationFilter -> UsernamePasswordFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义的用户名密码认证类
        http.authenticationProvider(usernamePasswordProvider);
        return http.build();
    }

    /**
     * 输入错误密码三次将需要输入验证码
     */
    @Bean
    public CaptchaAuthenticationDetailsSource authenticationDetailsSource() {
        return new CaptchaAuthenticationDetailsSource();
    }

    /**
     * 自定义用户名密码验证规则
     */
    @Bean
    public AuthenticationProvider usernamePasswordProvider() {
        return new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder,
                redisHelper, userService, registeredClientService);
    }

}
