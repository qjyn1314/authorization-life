package com.authorization.utils.security;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;
import java.util.TreeSet;

/**
 * 登录配置信息
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "sso.security")
public class SsoSecurityProperties implements SecurityCoreService {

    /**
     * 是否开启认证,默认开启
     */
    private Boolean enable = true;

    /**
     * gateway与后续服务签署用户信息jwt时使用的密钥, 如果没有则使用默认的
     */
    private String secret;

    public String getSecret() {
        return StrUtil.isBlank(secret) ? JwtService.DEFAULT_SECRET : secret;
    }

    /**
     * 忽略认证的url
     */
    private Set<String> ignorePermUrls = new TreeSet<>();

    public Set<String> getIgnorePermUrls() {
        Set<String> permUrls = ignorePermUrls;
        permUrls.addAll(Set.of(SecurityCoreService.IGNORE_PERM_URLS));
        return permUrls;
    }
}
