package com.authserver.common.life.security;

import com.authserver.common.life.filter.JwtAuthenticationFilter;
import com.authserver.common.life.security.handler.sso.SsoFailureHandler;
import com.authserver.common.life.security.handler.sso.SsoSuccessHandler;
import com.authserver.common.life.security.sso.CaptchaAuthenticationDetailsSource;
import com.authserver.common.life.security.sso.UsernamePasswordAuthenticationProvider;
import com.authserver.common.life.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private RedisTemplate<String, Object> redisHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private OAuth2AuthorizationService authorizationService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          JwtAuthenticationFilter jwtAuthenticationFilter,
                                                          CaptchaAuthenticationDetailsSource authenticationDetailsSource)
            throws Exception {
        http
                // 禁用csrf
                .csrf().disable()
                // 使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                // 无需认证即可访问
                // swagger文档
                .antMatchers("/v2/api-docs").permitAll()
                // 公有public路径
                .antMatchers("/public/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/login/**").permitAll()

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/auth/logout")
//                .addLogoutHandler(new SsoLogoutHandle(authorizationService, redisHelper))
                .and()
                .formLogin()
                .loginProcessingUrl(SecurityContant.SSO_LOGIN)
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(new SsoSuccessHandler())
                .failureHandler(new SsoFailureHandler());
        // 过滤器顺序为 sessionFilter -> UsernamePasswordFilter
        // 添加jwtfilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加用户名密码认证
        http.authenticationProvider(new UsernamePasswordAuthenticationProvider(userDetailsService, passwordEncoder,
                redisHelper, userService));
        return http.build();
    }

    @Bean
    public CaptchaAuthenticationDetailsSource authenticationDetailsSource() {
        return new CaptchaAuthenticationDetailsSource();
    }

//    @Bean
//    public AuthenticationConverter authenticationConverter() {
//        return new OAuth2ImplicitAuthenticationConverter();
//    }

    /**
     * 使用推荐密码策略
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 拦截器
     */
    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        return new JwtAuthenticationFilter();
    }


}
