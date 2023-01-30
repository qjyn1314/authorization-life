package com.authorization.utils.security;

import com.authorization.utils.kvp.KvpFormat;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public interface SecurityConstant {

    String DEFAULT_DOMAIN = "www.authorization.life";
    /**
     * token在签发的时候使用的发行域名
     */
    String ISSUER = "https://authorization.life";
    /**
     * 登录页的跳转路径
     */
    String SSO_LOGIN_FORM_PAGE = "/login";
    /**
     * 请求登录接口
     */
    String SSO_LOGIN = "/oauth/login";
    /**
     * 密码输入错误的次数,redis的key
     */
    String PASSWORD_ERROR_COUNT = "oauth-server:auth:password-error-count:{username}";
    /**
     * 登录过程中所需要存储信息的 redisKey前缀
     */
    String AUTHORIZATION = "oauth-server:auth:oauth2:authorization";

    /**
     * 生成token时用于组装 authorizationId的redisKey
     */
    static String getAuthorizationId(String authorizationId) {
        return AUTHORIZATION + "_" + authorizationId;
    }

    /**
     * 登录同意的信息存储key
     */
    String AUTHORIZATION_CONSENT = "oauth-server:auth:oauth2:authorization-consent";
    /**
     * oauth2/token接口中返回的 accessToken(jwt形式)中claim 的key
     */
    String CLAIM_TOKEN_KEY = "token";
    /**
     * accessToken 过期时间, 单位:秒(24小时)
     */
    long ACCESS_TOKEN_TIME_TO_LIVE = 86400;
    /**
     * refreshToken 过期时间, 单位:秒(30小时)
     */
    long REFRESH_TOKEN_TIME_TO_LIVE = 108000;
    /**
     * 授权码模式中的回调地址: AUTHORIZATION_CODE
     */
    String AUTHORIZATION_CODE_IMPLICIT_REDIRECT_URI_FORMAT = "{redirectUri}" +
            "#access_token={accessToken}" +
            "&token_type={tokenType}" +
            "&expires_in={expiresIn}" +
            "&state={state}" +
            "&scope={scope}";
    /**
     * 登录成功的用户信息缓存
     */
    String USER_TOKEN_DETAIL = "authorization:auth-server:user:{token}";

    /**
     * redis中存储的用户tokenKey信息
     */
    static String getUserTokenKey(String token) {
        return KvpFormat.of(USER_TOKEN_DETAIL).add(CLAIM_TOKEN_KEY, token).format();
    }

    /**
     * 退出登录接口
     */
    String SSO_LOGOUT = "/oauth/logout";
    /**
     * 退出登录是需要将sessionId删除, 如果有的情况下
     */
    String JSESSIONID = "JSESSIONID";

    /**
     * 前端传参的accessToken参数名称
     */
    String ACCESS_TOKEN = "access_token";

    String USER_DETAIL = "oauth-server:auth:user:{token}";
    String AUTH_PERM = "oauth-server:auth:perm:{serviceName}:{httpMethod}";
    String PERM_ROLE = "oauth-server:auth:perm-role:{permCode}";
    String USER_ROLE_MENU = "oauth-server:auth:menu:{token}:{roleCode}";
    String TOKEN_STORE = "oauth-server:auth:token-store";
    String REMEMBER_ME_PARAM = "remember_me";
    int COOKIE_TIMEOUT = 86400;
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
            //获取client信息接口
            "/oauth2/**",
            //密码登录接口
            "/oauth/login",
            //退出登录接口
            "/oauth/logout",
            //aouth2接口
            "/oauth2/**",
    };

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


}
