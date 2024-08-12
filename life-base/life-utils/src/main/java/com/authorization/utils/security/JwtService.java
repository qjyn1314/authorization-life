package com.authorization.utils.security;

import java.util.Map;

/**
 * <p>
 * jwt工具接口,此处将使用 nimbus-jose-jwt .
 * <p>
 * <a href="https://blog.csdn.net/qq_45240568/article/details/128262807">nimbus-jose-jwt的使用博客-001</a>
 * <p>
 * <a href="https://blog.csdn.net/dghkgjlh/article/details/121665795">nimbus-jose-jwt的使用博客-002</a>
 * </p>
 * <a href="https://jwt.io/introduction">JWT官网的解释和说明, 即JWT字符串中包含了什么,有什么作用,如何创建.</a>
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-03 18:35
 */
public interface JwtService {
    /**
     * 默认的jwt密钥
     */
    String DEFAULT_SECRET = "q1zrLbKQ1EkFR7X3iQcUL/3Z50OU3lKn1h9ZpILp0/w=";

    /**
     * 创建jwttoken字符串
     *
     * @param secret 密钥
     * @param claims 数据
     * @return String
     */
    String createJwtToken(String secret, Map<String, Object> claims);

    /**
     * 验证token
     */
    boolean validateJwtToken(String jwtToken);

    /**
     * 解析token
     */
    Map<String, Object> getClaimsFromJwtToken(String jwtToken);

}
