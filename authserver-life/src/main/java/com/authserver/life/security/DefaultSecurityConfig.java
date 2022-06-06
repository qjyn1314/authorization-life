package com.authserver.life.security;

import com.authserver.common.filter.JwtAuthenticationFilter;
import com.authserver.life.security.handler.sso.SsoFailureHandler;
import com.authserver.life.security.handler.sso.SsoSuccessHandler;
import com.authserver.life.security.sso.CaptchaAuthenticationDetailsSource;
import com.authserver.life.security.sso.UsernamePasswordAuthenticationProvider;
import com.authserver.life.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class DefaultSecurityConfig {

    @Autowired
    private RedisTemplate<String, Object> redisHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private OAuth2AuthorizationService authorizationService;
    @Autowired
    private RegisteredClientRepository registeredClientService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
                                                          CaptchaAuthenticationDetailsSource authenticationDetailsSource,
                                                          JwtAuthenticationFilter jwtAuthenticationFilter,
                                                          AuthenticationProvider usernamePasswordProvider)
            throws Exception {
        http
                // 禁用csrf-取消csrf防护-参考：https://blog.csdn.net/yjclsx/article/details/80349906
                .csrf().disable()
                // 使用session
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
                .invalidateHttpSession(true)
                .clearAuthentication(true)
//                .addLogoutHandler(new SsoLogoutHandle(authorizationService, redisHelper))

                .and()
                .formLogin()
                .loginProcessingUrl(SecurityContant.SSO_LOGIN)
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(new SsoSuccessHandler())
                .failureHandler(new SsoFailureHandler());
//        http.apply()//此处可以添加短信验证码的配置。
        // 添加自定义的用户名密码认证
        http.authenticationProvider(usernamePasswordProvider);
        // 添加jwtfilter  过滤器顺序为 jwtAuthenticationFilter -> UsernamePasswordFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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

//    @Bean
//    public AuthenticationConverter authenticationConverter() {
//        return new OAuth2ImplicitAuthenticationConverter();
//    }

}
