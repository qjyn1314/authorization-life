package com.authorization.life.security;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public final class SecurityConstant {
    public static final String USER_DETAIL = "oauth-server:auth:user:{token}";
    public static final String AUTH_PERM = "oauth-server:auth:perm:{serviceName}:{httpMethod}";
    public static final String PERM_ROLE = "oauth-server:auth:perm-role:{permCode}";
    public static final String USER_ROLE_MENU = "oauth-server:auth:menu:{token}:{roleCode}";
    public static final String TOKEN_STORE = "oauth-server:auth:token-store";
    public static final String PASSWORD_ERROR_COUNT = "oauth-server:auth:password-error-count:{username}";
    public static final String SSO_LOGIN = "/oauth/login";
    public static final String SSO_LOGOUT = "/oauth/logout";
    public static final String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";
    public static final String AUTHORIZATION_CONSENT = "oauth-server:auth:oauth2:authorization-consent";
    public static final String ISSUER = "https://www.authorization.life";
    public static final String IMPLICIT_REDIRECT_URI_FORMAT = "{redirectUri}" +
            "#access_token={accessToken}" +
            "&token_type={tokenType}" +
            "&expires_in={expiresIn}" +
            "&state={state}" +
            "&scope={scope}";
    public static final String MOBILE_KEY = "phone";
    public static final String PHONE_LOGIN = "phone-login";


}
