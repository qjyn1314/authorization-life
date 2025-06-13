package com.authorization.life.security.start.config;

import com.authorization.life.security.start.filter.JwtAuthenticationFilter;
import com.authorization.life.security.start.service.UserDetailsServiceImpl;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SsoSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2022/5/29 11:13
 */
@AutoConfiguration
public class SecurityAutoComponent {


    @Bean
    @ConditionalOnMissingBean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * 使用推荐密码策略
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 此处将配置每次只执行一次的filter，需要对gateway中jwt进行解析为内部的当前登录用户信息
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(SsoSecurityProperties ssoSecurityProperties, JwtService jwtService) {
        return new JwtAuthenticationFilter(ssoSecurityProperties, jwtService);
    }

}
