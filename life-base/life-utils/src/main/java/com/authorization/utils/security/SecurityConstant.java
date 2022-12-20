package com.authorization.utils.security;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public interface SecurityConstant {
    String USER_DETAIL = "oauth-server:auth:user:{token}";
    String AUTH_PERM = "oauth-server:auth:perm:{serviceName}:{httpMethod}";
    String PERM_ROLE = "oauth-server:auth:perm-role:{permCode}";
    String USER_ROLE_MENU = "oauth-server:auth:menu:{token}:{roleCode}";
    String TOKEN_STORE = "oauth-server:auth:token-store";
    String PASSWORD_ERROR_COUNT = "oauth-server:auth:password-error-count:{username}";
    String SSO_LOGIN = "/oauth/login";
    String SSO_LOGIN_FORM_PAGE = "/login";
    String JSESSIONID = "JSESSIONID";
    String SSO_LOGOUT = "/oauth/logout";
    String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";
    String AUTHORIZATION_CONSENT = "oauth-server:auth:oauth2:authorization-consent";
    String ISSUER = "https://authorization.life";
    String IMPLICIT_REDIRECT_URI_FORMAT = "{redirectUri}" +
            "#access_token={accessToken}" +
            "&token_type={tokenType}" +
            "&expires_in={expiresIn}" +
            "&state={state}" +
            "&scope={scope}";
    String MOBILE_KEY = "phone";
    String PHONE_LOGIN = "phone-login";

    /**
     * 无需认证即可访问的请求路径
     */
    String[] IGNORE_PERM_URLS = {
            //swagger文档
            "/v3/api-docs",
            "/doc.html",
            "/webjars/**",
            //公有public路径
            "/public/**",
            //监控服务路径
            "/actuator/**",
            //sql监控控制台
            "/druid/**",
            //密码登录
            "/oauth/login/**",
            //退出登录接口
            "/oauth/revoke",
            //aouth2接口
            "/oauth2/**",
    };


}
