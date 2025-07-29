package com.authorization.life.auth.app.vo;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * 授权类型返回参数
 *
 * @author wangjunming
 * @since 2025-07-28 17:30
 */
@Slf4j
@Getter
@Setter
@ToString
public class AuthorizationGrant implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 客户端ID */
  private String clientId;

  /** 客户端认证密钥 */
  private String clientSecret;

  /** 客户端类型 */
  private String clientType;

  /** 授权类型 */
  private String grantType;

  /** 授权类型描述 */
  private String grantTypeContent;

  /** 授权类型协议地址 */
  private List<String> grantTypeAgreement;

  /** 授权步骤 */
  private String stepNum;

  /** 授权url */
  private String grantTypeUrl;

  /** 请求方式 */
  private String method;

  /** 请求内容类型 */
  private String contentType;

  /** 请求参数 */
  private Map<String, String> params = new HashMap<>();

  /** 重定向地址 */
  private String redirectUri;

  /** 授权类型的请求步骤 */
  private TreeSet<AuthorizationGrant> grantTypeSet =
      Sets.newTreeSet(Comparator.comparing(item -> item.getStepNum() + "_" + item.getClientId()));

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof AuthorizationGrant that)) {
      return false;
    }
    return Objects.equal(getClientId(), that.getClientId())
        && Objects.equal(getClientSecret(), that.getClientSecret())
        && Objects.equal(getClientType(), that.getClientType())
        && Objects.equal(getGrantType(), that.getGrantType())
        && Objects.equal(getGrantTypeContent(), that.getGrantTypeContent())
        && Objects.equal(getStepNum(), that.getStepNum())
        && Objects.equal(getGrantTypeUrl(), that.getGrantTypeUrl())
        && Objects.equal(getMethod(), that.getMethod());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        getClientId(),
        getClientSecret(),
        getClientType(),
        getGrantType(),
        getGrantTypeContent(),
        getStepNum(),
        getGrantTypeUrl(),
        getMethod());
  }

  public static class GrantTypeEnum {
    /** 授权码模式 */
    public static final String AUTHORIZATION_CODE =
        AuthorizationGrantType.AUTHORIZATION_CODE.getValue();

    /** 隐式模式 */
    public static final String IMPLICIT = "implicit";

    /** 客户端凭证模式 */
    public static final String CLIENT_CREDENTIALS =
        AuthorizationGrantType.CLIENT_CREDENTIALS.getValue();

    /** 密码模式 */
    public static final String PASSWORD = "password";

    /** 邮箱验证码模式 */
    public static final String EMAIL_CAPTCHA = "email_captcha";

    /** 手机验证码模式 */
    public static final String PHONE_CAPTCHA = "phone_captcha";

    /** 刷新AccessToken */
    public static String REFRESH_TOKEN = AuthorizationGrantType.REFRESH_TOKEN.getValue();

    public static final String AUTHORIZATION_CODE_ENHANCE = AUTHORIZATION_CODE + "_pkce";
    public static final String JWT_BEARER = AuthorizationGrantType.JWT_BEARER.getValue();
    public static final String DEVICE_CODE = AuthorizationGrantType.DEVICE_CODE.getValue();
    public static final String TOKEN_EXCHANGE = AuthorizationGrantType.TOKEN_EXCHANGE.getValue();

    // --- oauth2.0 协议地址: https://datatracker.ietf.org/doc/html/rfc6749
    // --- oauth2.1 协议地址: https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-13
    // --- https://developer.atlassian.com/cloud/jira/software/user-impersonation-for-connect-apps/
    public static final String Oauth20Agreement = "https://datatracker.ietf.org/doc/html/rfc6749";
    public static final String Oauth21Agreement =
        "https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-13";

    public static final String Oauth20Type = " OAuth 2.0";
    public static final String Oauth21Type = " OAuth 2.1";

    public static final String stepNumOne = "第1步";
    public static final String stepNumTwo = "第2步";
    public static final String stepNumThree = "第3步";

    public static final String GET = "GET";
    public static final String POST = "POST";

    public static final String S256 = "S256";
    public static final String PLAIN = "plain";

    public static final String FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static final TreeMap<String, String> GRANT_TYPES = Maps.newTreeMap();
    public static final TreeMap<String, String> GRANT_TYPES_OAUTH21 = Maps.newTreeMap();

    static {
      GRANT_TYPES.put(AUTHORIZATION_CODE, "授权码模式" + Oauth20Type);
      GRANT_TYPES.put(IMPLICIT, "隐式模式" + Oauth20Type);
      GRANT_TYPES.put(CLIENT_CREDENTIALS, "客户端凭证模式" + Oauth20Type);
      GRANT_TYPES.put(PASSWORD, "密码模式" + Oauth20Type);
    }

    static {
      GRANT_TYPES_OAUTH21.put(AUTHORIZATION_CODE, "授权码模式" + Oauth21Type);
      GRANT_TYPES_OAUTH21.put(EMAIL_CAPTCHA, "邮箱验证码模式" + Oauth21Type);
      GRANT_TYPES_OAUTH21.put(PHONE_CAPTCHA, "手机验证码模式" + Oauth21Type);
      GRANT_TYPES_OAUTH21.put(JWT_BEARER, "jwt-bearer" + Oauth21Type);
      GRANT_TYPES_OAUTH21.put(DEVICE_CODE, "device_code" + Oauth21Type);
      GRANT_TYPES_OAUTH21.put(TOKEN_EXCHANGE, "token-exchange" + Oauth21Type);
    }
  }
}
