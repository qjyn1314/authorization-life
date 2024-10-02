package com.authorization.life.auth.infra.entity;

import com.authorization.mybatis.start.entity.AuditEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("liam_oauth_client")
public class OauthClient extends AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_OAUTH_CLIENT_ID = "oauthClientId";
    public static final String FIELD_DOMAIN_NAME = "domainName";
    public static final String FIELD_CLIENT_ID = "clientId";
    public static final String FIELD_CLIENT_SECRET = "clientSecret";
    public static final String FIELD_GRANT_TYPES = "grantTypes";
    public static final String FIELD_SCOPES = "scopes";
    public static final String FIELD_REDIRECT_URI = "redirectUri";
    public static final String FIELD_ACCESS_TOKEN_TIMEOUT = "accessTokenTimeout";
    public static final String FIELD_REFRESH_TOKEN_TIMEOUT = "refreshTokenTimeout";
    public static final String FIELD_ADDITIONAL_INFORMATION = "additionalInformation";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CLIENT_SECRET_BAK = "clientSecretBak";


    /**
     * oauth客户端表主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String oauthClientId;
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
    /**
     * 客户端密钥
     */
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
