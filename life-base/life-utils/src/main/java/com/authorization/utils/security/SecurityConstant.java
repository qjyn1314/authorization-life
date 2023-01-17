package com.authorization.utils.security;

import com.authorization.utils.kvp.KvpFormat;

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
    String REMEMBER_ME_PARAM = "remember_me";
    int COOKIE_TIMEOUT = 86400;

    static String getAuthorizationId(String authorizationId) {
        return AUTHORIZATION + "_" + authorizationId;
    }

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


    /**
     * 前端的jwtToken中claim 的key
     */
    String TOKEN = "token";

    /**
     * 用户信息缓存
     */
    String USER_TOKEN_DETAIL = "authorization:auth-server:user:{token}";

    /**
     * redis中存储的用户tokenKey信息
     */
    static String getUserTokenKey(String token) {
        return KvpFormat.of(USER_TOKEN_DETAIL)
                .add(TOKEN, token).format();
    }

    /**
     * 前端传参的accessToken参数名称
     */
    String ACCESS_TOKEN = "access_token";

    /**
     * 开启记住我的功能中所使用的域名
     */
    String SECURITY_DOMAIN = "authorization.life";

    /**
     * saas体系模块中所有的http头文件
     */
    class Header {
        public static final String AUTH_POSITION = "Z-Auth-Position";
        public static final String TYPE_BEARER = "bearer";
    }

    /**
     * accessToken 过期时间, 单位:秒(24小时)
     */
    long ACCESS_TOKEN_TIME_TO_LIVE = 86400;
    /**
     * refreshToken 过期时间, 单位:秒(30小时)
     */
    long REFRESH_TOKEN_TIME_TO_LIVE = 108000;

}
