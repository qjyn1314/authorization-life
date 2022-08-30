package com.authorization.core.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 登录配置信息
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "sso.security")
public class SsoSecurityProperties {

    private String ssoLoginPage;

}
