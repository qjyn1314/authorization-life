package com.authorization.utils.security;

import com.authorization.utils.jwt.Jwts;
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

    /**
     * 是否开启认证,默认开启
     */
    private Boolean enable = true;

    /**
     * gateway与后续服务签署用户信息jwt使用的密钥, 如果没有则使用默认的
     */
    private String secret = Jwts.DEFAULT_SECRET;

}
