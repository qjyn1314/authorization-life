package com.authorization.utils.security;

import com.authorization.utils.StringUtil;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <p>
 * 权限常量类
 * </p>
 */
public interface SecurityCoreService {

    /**
     * 默认使用此域名查询client信息
     */
    String DEFAULT_DOMAIN = "www.authorization.life";
    /**
     * 默认使用此域名查询client信息
     */
    String DEFAULT_COOKIE_DOMAIN = "authorization.life";
    /**
     * token在签发的时候使用的发行域名
     */
    String DEFAULT_ISSUER = "https://authorization.life";
    /**
     * accessToken参数的key
     */
    String ACCESS_TOKEN = "access_token";
    /**
     * accessToken参数类型的key
     */
    String TOKEN_TYPE = "token_type";
    /**
     * 请求头中AccessToken的Key
     */
    String AUTHORIZATION = "Authorization";
    /**
     * 请求头中AccessToken的Key
     */
    String AUTHORIZATION_TOKEN = "AuthorizationToken";
    /**
     * 请求头中AccessToken的Key的前缀
     */
    String ACCESS_TOKEN_TYPE = "Bearer";
    /**
     * gateway下发到各个服务的TokenHeader, 为了确定系统中的所有请求一定是从此gateway中请求, 从而获取到用户信息.
     */
    String AUTH_POSITION = "Auth-Position";

    String AUTH_FORWARDED = "x-forwarded-prefix";
    /**
     * 退出登录是需要将sessionId删除, 如果有的情况下
     */
    String JSESSIONID = "JSESSIONID";
    /**
     * 登录页的跳转路径
     */
//    String SSO_LOGIN_FORM_PAGE = "/login";
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
    String SSO_SMS_LOGIN = "/oauth/sms-login";
    /**
     * 用户邮箱+邮箱验证码登录请求的接口
     */
    String SSO_EMAIL_LOGIN = "/oauth/email-login";
    /**
     * 验证码的缓存key
     */
    String CAPTCHA_CACHE_KEY = "sso-oauth-server:auth:captcha-code:{uuid}";
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
     * 登录过程中所需要存储信息的 redisKey前缀
     */
    String AUTHORIZATION_KET_PREFIX = "sso-oauth-server:auth:token:";
    /**
     * oauth2/token 接口中返回的 accessToken(jwt形式)中用户的标识 在 claim 的key
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
        return StringUtil.of(USER_TOKEN_DETAIL).add(CLAIM_TOKEN_KEY, token).format();
    }


    /**
     * 默认情况下无需认证即可访问的请求路径
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
            // 登录页/注册页/忘记密码页面的请求
            "/oauth/**",
            // 数据库监控
            "/druid/**",
            // 临时code获取token
            "/oauth2/token",
            // 授权页面
//            "/auth-redirect",
    };

}
