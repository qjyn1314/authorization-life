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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
@Slf4j
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
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

        // 前后端分离工程需要 禁用csrf-取消csrf防护-参考：https://blog.csdn.net/yjclsx/article/details/80349906
        http.csrf(AbstractHttpConfigurer::disable);

        // 配置session会话管理器
        http.sessionManagement(sessionMan -> sessionMan
                // 此处必须配置为  security 在需要的时候创建session, 因为 OAuth2AuthorizationServer 时通过当前session会话中获取登录用户信息的.
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                // 仅允许一个用户登录
                .maximumSessions(1)
                // session过期后的处理策略
                .expiredSessionStrategy(new TokenInformationExpiredStrategy()));

        http.authorizeHttpRequests(authHttpReq -> authHttpReq
                // 无需认证即可访问
                .requestMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
        );

        // 配置异常情况的处理器
        http.exceptionHandling(exceHandle ->
                exceHandle
                        //未登录时请求访问接口所需要跳转的自定义路径，即没有登录时将直接跳转到此 url中
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint()));

        // 配置退出登录配置
        http.logout(logout -> logout
                // 指定退出登录url
                .logoutUrl(SecurityConstant.SSO_LOGOUT)
                // 自定义退出登录处理器,根据当前登录用户的信息删除缓存中的数据
                .addLogoutHandler(new SsoLogoutHandle(authorizationService, stringRedisService, ssoSecurityProperties))
                //在此处可以删除相应的cookie
                .deleteCookies(SecurityConstant.JSESSIONID)
                //需要验证session
                .invalidateHttpSession(true)
                //清楚认证权限
                .clearAuthentication(true));

        // 配置表单登录配置
        http.formLogin(form -> form
                // 指定form表单登录的路径
                .loginProcessingUrl(SecurityConstant.SSO_LOGIN)
                // 配置 达到登录失败次数时, 需要填写的验证码等信息
                .authenticationDetailsSource(authenticationDetailsSource)
                // 表单登录成功处理器
                .successHandler(new SsoSuccessHandler())
                // 表单登录失败处理器, 用于返回
                .failureHandler(new SsoFailureHandler())
        );
        // 添加jwtfilter  过滤器顺序为 jwtAuthenticationFilter -> UsernamePasswordFilter , 其目的主要是为了每次验证解析jwtToken为当前登录用户并设置到当前sesurity会话中
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加自定义的用户名密码认证类
        http.authenticationProvider(usernamePasswordProvider);
        //开启记住我的功能，默认时间是24小时
//        http.rememberMe()
//                .alwaysRemember(Boolean.TRUE)
//                //记住我的form表单传参
//                .rememberMeParameter(SecurityConstant.REMEMBER_ME_PARAM)
//                //cookie的过期秒数
//                .tokenValiditySeconds(SecurityConstant.COOKIE_TIMEOUT)
//                //cookie中的名称
//                .rememberMeCookieName(SecurityConstant.REMEMBER_ME_PARAM)
//                //需要配置的二级域名
//                .rememberMeCookieDomain(SecurityConstant.SECURITY_DOMAIN)
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

    /**
     * 配置生成 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationProvider usernamePasswordProvider) throws Exception {
        AuthenticationManagerBuilder authMagBuild = http.getSharedObject(AuthenticationManagerBuilder.class);
        authMagBuild.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        authMagBuild.authenticationProvider(usernamePasswordProvider);
        return authMagBuild.build();
    }

}
