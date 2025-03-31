package com.authorization.life.auth.app.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * oauth客户端表的返回值
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OauthClientVO implements Serializable {

    /**
     * 客户端域名
     */
    private String domainName;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    private String clientSecretBak;
    /**
     * 授权类型
     */
    private String grantTypes;
    /**
     * 授权域
     */
    private String scopes;
    /**
     * 重定向地址
     */
    private String redirectUri;
    /**
     * 重定向地址
     */
    private String redirectUrl;
    /**
     * 访问授权超时时间
     */
    private Integer accessTokenTimeout;
    /**
     * 刷新授权超时时间
     */
    private Integer refreshTokenTimeout;
    /**
     * 附加信息
     */
    private String additionalInformation;

    /**
     * 租户ID
     */
    private String tenantId;
}
