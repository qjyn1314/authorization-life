package com.authserver.life.security;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public final class SecurityConstant {
    //-----------------------------------
    public static final String USER_DETAIL = "auth-server:auth:user:{token}";
    public static final String AUTH_PERM = "auth-server:auth:perm:{serviceName}:{httpMethod}";
    public static final String PERM_ROLE = "auth-server:auth:perm-role:{permCode}";
    public static final String USER_ROLE_MENU = "auth-server:auth:menu:{token}:{roleCode}";
    public static final String TOKEN_STORE = "auth-server:auth:token-store";
    public static final String PASSWORD_ERROR_COUNT = "auth-server:auth:password-error-count:{username}";
    //-----------------------------------
    public static final String SSO_LOGIN = "/login";
    //-----------------------------------
    public static final String AUTHORIZATION = "auth-server:auth:oauth2:authorization";
    public static final String AUTHORIZATION_CONSENT = "auth-server:auth:oauth2:authorization-consent";
    //-----------------------------------
    public static final String ISSUER = "http://auth-server.com";
    public static final String IMPLICIT_REDIRECT_URI_FORMAT = "{redirectUri}" +
            "#access_token={accessToken}" +
            "&token_type={tokenType}" +
            "&expires_in={expiresIn}" +
            "&state={state}" +
            "&scope={scope}";
    //-----------------------------------
    public static final String MOBILE_KEY = "phone";
    public static final String PHONE_LOGIN = "phone-login";


}
