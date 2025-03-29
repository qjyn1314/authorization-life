package com.authorization.core.security.config;

import com.authorization.core.security.filter.JwtAuthenticationFilter;
import com.authorization.utils.security.SecurityCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * spring security配置  在认证服务中需要排除此配置信息。即 exclude = {SecurityAutoConfiguration.class}
 * EnableGlobalMethodSecurity 注解详解：
 * https://blog.csdn.net/chihaihai/article/details/104678864
 *
 * @author wangjunming
 */
@AutoConfiguration
@EnableWebSecurity
public class SecurityAutoConfiguration {
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain defaultSpringSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(CorsConfigurer::disable).csrf(CsrfConfigurer::disable);
        // 设置禁用session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorizeRequests -> {
            // 配置请求路径不需要鉴权认证
            authorizeRequests.requestMatchers(SecurityCoreService.IGNORE_PERM_URLS).permitAll();
            // 除上面外的所有请求全部需要鉴权认证
            authorizeRequests.anyRequest().authenticated();
        });
        http.exceptionHandling(exception -> {
            //未登录时跳转至登录页面
            exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(SecurityCoreService.SSO_LOGIN_FORM_PAGE));
        });
        // 配置formLogin登录, 是为了添加 UsernamePasswordAuthenticationFilter 到过滤链中.
        http.formLogin(Customizer.withDefaults());
        // 过滤器顺序为 jwtFilter -> UsernamePasswordFilter  ，此处是配置的原因是将每次请求头中的token信息转换为SecurityContent
        // 添加jwtfilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        http.addFilterBefore(corsFilter, JwtAuthenticationFilter.class);
        http.addFilterBefore(corsFilter, LogoutFilter.class);
        return http.build();
    }

}
