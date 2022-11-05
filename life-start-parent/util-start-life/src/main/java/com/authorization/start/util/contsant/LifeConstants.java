package com.authorization.start.util.contsant;

/**
 * 公共的相关常量
 */
public class LifeConstants {

    /**
     * 用户信息缓存
     */
    public static final String USER_DETAIL = "authorization:auth-server:user:{token}";


    /**
     * saas体系http头文件
     */
    public static class Header {
        public static final String AUTH_POSITION = "Z-Auth-Position";
        public static final String TYPE_BEARER = "bearer";
    }

    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN = "token";
    public static final String SECURITY_DOMAIN = "authorization.life";
}
