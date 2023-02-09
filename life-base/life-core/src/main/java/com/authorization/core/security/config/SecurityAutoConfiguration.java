package com.authorization.core.security.config;

import com.authorization.core.security.filter.JwtAuthenticationFilter;
import com.authorization.core.security.handle.LoginUrlAuthenticationEntryPoint;
import com.authorization.core.security.handle.TokenInformationExpiredStrategy;
import com.authorization.utils.security.SecurityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
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

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    public SecurityFilterChain defaultSpringSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 禁用csrf
                .csrf().disable()
                // 使用session 如果security需要设置sessionid则进行设置。  参考：https://blog.csdn.net/zpz_326/article/details/80901575
                //限制同一账号只能一个用户使用
                .sessionManagement().maximumSessions(1)
                .expiredSessionStrategy(new TokenInformationExpiredStrategy())
                .and()
                // 对于全局的配置项中将设置 为不需要 使用session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                // 无需认证即可访问
                .requestMatchers(SecurityConstant.IGNORE_PERM_URLS).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                //未登录时请求访问接口所需要跳转的自定义路径，即没有登录时将直接跳转到此 url中
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint());

        httpSecurity.formLogin(Customizer.withDefaults());
        // 过滤器顺序为 jwtFilter -> UsernamePasswordFilter  ，此处是配置的原因是将每次请求头中的token信息转换为SecurityContent
        // 添加jwtfilter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
        return httpSecurity.build();
    }

}
