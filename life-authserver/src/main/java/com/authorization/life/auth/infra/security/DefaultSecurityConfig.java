package com.authorization.life.auth.infra.security;

import com.authorization.core.security.UserDetailService;
import com.authorization.core.security.filter.JwtAuthenticationFilter;
import com.authorization.core.security.handle.LoginUrlAuthenticationEntryPoint;
import com.authorization.core.security.handle.TokenInformationExpiredStrategy;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.infra.security.handler.sso.SsoFailureHandler;
import com.authorization.life.auth.infra.security.handler.sso.SsoLogoutHandle;
import com.authorization.life.auth.infra.security.handler.sso.SsoSuccessHandler;
import com.authorization.life.auth.infra.security.sso.CaptchaAuthenticationDetailsSource;
import com.authorization.life.auth.infra.security.sso.UsernamePasswordAuthenticationProvider;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.security.SecurityConstant;
import com.authorization.utils.security.SsoSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
 *
 * @author wangjunming
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private StringRedisService stringRedisService;
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

    @Autowired
    private RedisTemplate redisTemplate;

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
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        // 设置认证的路径
        http.authorizeHttpRequests()
                // 无需认证即可访问
                .requestMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                //未登录时请求访问接口所需要跳转的自定义路径，即没有登录时将直接跳转到此 url中
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint());
        // 配置退出登录配置
        http.logout()
                .logoutUrl(SecurityConstant.SSO_LOGOUT)
                .addLogoutHandler(new SsoLogoutHandle(authorizationService, stringRedisService, ssoSecurityProperties, redisTemplate))
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
//        //开启记住我的功能，默认时间是两周
//        http.rememberMe()
//                .alwaysRemember(Boolean.TRUE)
////                .rememberMeParameter(AuthUserUtil.FORM_REMEMBER_ME_COOKIE_NAME)
//                //cookie的过期秒数
////                .tokenValiditySeconds(AuthUserUtil.AUTH_COOKIE_TIME)
//                //form表单中的记住我input框的name属性值
////                .rememberMeCookieName(AuthUserUtil.FORM_REMEMBER_ME_COOKIE_NAME)
//                //需要配置的二级域名
//                .rememberMeCookieDomain(LifeSecurityConstants.SECURITY_DOMAIN)
//                //配置用户service，用于在关闭浏览器再次打开时，使用此service型数据库中根据名称查询用户数据，
//                .userDetailsService(userDetailsService);
        return http.build();
    }

    /**
     * 输入错误密码三次将需要输入的验证码参数
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
                stringRedisService, userService, registeredClientService);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .authenticationProvider(usernamePasswordProvider())
                .build();
    }

}
