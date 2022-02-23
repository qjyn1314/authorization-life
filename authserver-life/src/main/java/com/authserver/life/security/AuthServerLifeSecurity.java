package com.authserver.life.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * <p>
 *    oauth2 security配置类
 * </p>
 *
 * @author wangjunming
 * @since 2022/2/23 16:30
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AuthServerLifeSecurity {




}
