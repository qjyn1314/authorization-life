package com.authorization.core.security.config;

import com.authorization.core.security.filter.JwtAuthenticationFilter;
import com.authorization.core.security.service.UserDetailsServiceImpl;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SsoSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 此处需要注意跨域问题: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
        // 即: 当 allowCredentials 为 true 时，allowedOrigins 不能包含特殊值“*”，因为不能在“访问控制-允许-源”响应标头上设置该值。要允许一组源的凭据，请显式列出它们或考虑改用“allowedOriginPatterns”。
        // 设置访问源地址
//        config.addAllowedOrigin(CorsConfiguration.ALL);
        // 设置访问源地址
        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        // 设置访问源请求头
        config.addAllowedHeader(CorsConfiguration.ALL);
        // 设置访问源请求方法
        config.addAllowedMethod(CorsConfiguration.ALL);
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
