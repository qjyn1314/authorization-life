package com.authserver.common.life.security;

import com.authserver.common.life.UserDetailsServiceImpl;
import com.authserver.common.life.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * spring security配置  在认证服务中需要排除此配置信息。即 exclude = {SecurityAutoConfiguration.class}
 * EnableGlobalMethodSecurity 注解详解：
 * https://blog.csdn.net/chihaihai/article/details/104678864
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String LOGIN_URL = "/login";


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
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 禁用csrf
                .csrf().disable()
                // 使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 无需认证即可访问
                // swagger文档
                .antMatchers("/v2/api-docs").permitAll()
                // 公有public路径
                .antMatchers("/public/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/druid/**").permitAll()
                //密码登录
                .antMatchers("/auth/login").permitAll()
                //短信登录
                .antMatchers("/login/sms").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL));
        // 过滤器顺序为 jwtFilter -> UsernamePasswordFilter  ，此处是配置的原因是将每次请求头中的token信息转换为SecurityContent
        // 添加jwtfilter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Configuration
    static class SecurityComponent {

        @Bean
        @ConditionalOnMissingBean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }

        /**
         * 使用推荐密码策略
         */
        @Bean
        public PasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        /**
         * 此处将配置每次只执行一次的filter
         */
        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter();
        }

        /**
         * 跨域配置
         */
        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            // 设置访问源地址
            config.addAllowedOriginPattern("*");
            // 设置访问源请求头
            config.addAllowedHeader("*");
            // 设置访问源请求方法
            config.addAllowedMethod("*");
            // 对接口配置跨域设置
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }
    }
}
