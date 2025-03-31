package com.authorization.life.auth.app.dto;

import com.authorization.mybatis.start.entity.AuditEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * oauth客户端表
 * <p>
 * 表中的 domainName 与 clientId 分别独立为唯一索引
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Setter
@Getter
public class OauthClientDTO extends AuditEntity implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private String searchKey;
    /**
     * oauth客户端表主键
     */
    private String oauthClientId;
    /**
     * 客户端域名
     */
    @NotBlank(message = "客户端域名不能为空.")
    private String domainName;
    /**
     * 客户端ID
     */
    @NotBlank(message = "客户端ID不能为空.")
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 客户端密钥
     */
    @NotBlank(message = "客户端密钥不能为空.")
    private String clientSecretBak;
    /**
     * 回调URI
     */
    @NotBlank(message = "回调URI不能为空.")
    private String redirectUri;
    /**
     * 授权模式
     */
    @NotBlank(message = "授权模式不能为空.")
    private String grantTypes;
    /**
     * 授权域
     */
    @NotBlank(message = "授权域不能为空.")
    private String scopes;

    /**
     * 访问授权超时时间
     */
    @NotNull(message = "访问授权超时时间不能为空.")
    private Integer accessTokenTimeout;
    /**
     * 刷新授权超时时间
     */
    @NotNull(message = "刷新授权超时时间不能为空.")
    private Integer refreshTokenTimeout;
    /**
     * 附加信息
     */
    @NotNull(message = "附加信息不能为空.")
    private String additionalInformation;

}
