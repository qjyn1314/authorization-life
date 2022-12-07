package com.authorization.utils.contsant;

import com.authorization.utils.kvp.KvpFormat;

/**
 * 权限认证的相关常量
 *
 * @author wangjunming
 */
public class SecurityConstants {

    /**
     * 前端的jwtToken中claim 的key
     */
    public static final String TOKEN = "token";

    /**
     * 用户信息缓存
     */
    public static final String USER_DETAIL = "authorization:auth-server:user:{token}";

    /**
     * redis中存储的用户tokenKey信息
     */
    public static String getUserTokenKey(String token) {
        return KvpFormat.of(SecurityConstants.USER_DETAIL)
                .add(SecurityConstants.TOKEN, token).format();
    }

    /**
     * 前端传参的accessToken参数名称
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 开启记住我的功能中所使用的域名
     */
    public static final String SECURITY_DOMAIN = "authorization.life";

    /**
     * saas体系模块中所有的http头文件
     */
    public static class Header {
        public static final String AUTH_POSITION = "Z-Auth-Position";
        public static final String TYPE_BEARER = "bearer";
    }

}
