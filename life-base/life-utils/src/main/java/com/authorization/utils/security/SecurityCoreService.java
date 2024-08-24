package com.authorization.utils.security;

import com.authorization.utils.message.StrForm;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public interface SecurityCoreService {

    /**
     * 开启记住我的功能中所使用的域名
     */
    String DEFAULT_DOMAIN = "authorization.life";
    /**
     * token在签发的时候使用的发行域名
     */
    String DEFAULT_ISSUER = "https://authorization.life";
    /**
     * gateway下发到各个服务的TokenHeader, 其主要的目的是为了当前的应用确定的是从本系统中的gateway下发的请求.
     */
    String AUTH_POSITION = "Auth-Position";
    /**
     * 登录页的跳转路径
     */
    String SSO_LOGIN_FORM_PAGE = "/login";
    /**
     * 退出登录接口
     */
    String SSO_LOGOUT = "/oauth/logout";
    /**
     * 用户名密码(包含手机号,邮箱,用户名(username))登录请求的接口
     */
    String SSO_LOGIN = "/oauth/login";
    /**
     * 用户手机号+手机短信登录请求的接口
     */
    String SSO_SMS_LOGIN = "/oauth/smslogin";
    /**
     * 用户邮箱+邮箱验证码登录请求的接口
     */
    String SSO_EMAIL_LOGIN = "/oauth/emaillogin";
    /**
     * 验证码的缓存key
     */
    String CAPTCHA_CACHE_KEY = "sso-auth-server:auth:captcha-code:{uuid}";
    /**
     * 密码输入错误的次数,redis的key, 是指不管用什么方式登录出错都会被记录一次.当此处达到 默认5次时将锁定账户不允许登录.
     */
    String PASSWORD_ERROR_COUNT_KEY = "sso-oauth-server:auth:password-error-count:{username}";
    /**
     * 登录错误的默认次数
     */
    Integer ERROR_COUNT = 5;
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
     * 前端传参的accessToken参数名称
     */
    String ACCESS_TOKEN = "access_token";

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
     * oauth2/token接口中返回的 accessToken(jwt形式)中claim 的key
     */
    String CLAIM_TOKEN_KEY = "token";
    /**
     * 登录成功的用户信息缓存
     */
    String USER_TOKEN_DETAIL = "sso-oauth-server:user:{token}";
    /**
     * redis中存储的用户tokenKey信息
     */
    static String getUserTokenKey(String token) {
        return StrForm.of(USER_TOKEN_DETAIL).add(CLAIM_TOKEN_KEY, token).format();
    }


    /**
     * 退出登录是需要将sessionId删除, 如果有的情况下
     */
    String JSESSIONID = "JSESSIONID";
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
            "/v3/api-docs/**",
            "/favicon.ico",
            "/doc.html",
            "/webjars/**",
            //公有public路径
            "/public/**",
            //监控服务路径
            "/actuator/**",
            //获取client信息接口
            "/oauth/**",
            "/oauth2/**",
    };


}
