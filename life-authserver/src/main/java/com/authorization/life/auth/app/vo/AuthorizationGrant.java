package com.authorization.life.auth.app.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * <p>
 * 授权类型返回参数
 * </p>
 *
 * @author wangjunming
 * @since 2025-07-28 17:30
 */
@Slf4j
@Getter
@Setter
@ToString
public class AuthorizationGrant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端认证密钥
     */
    private String clientSecret;
    /**
     * 客户端类型
     */
    private String clientType;
    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 授权类型描述
     */
    private String grantTypeContent;

    /**
     * 授权类型协议地址
     */
    private String grantTypeAgreement;

    /**
     * 授权步骤
     */
    private String stepNum;

    public static final String stepNumOne = "第一步";
    public static final String stepNumTwo = "第二步";
    public static final String stepNumThree = "第三步";

    /**
     * 授权url
     */
    private String grantTypeUrl;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private Map<String, String> params = new HashMap<>();

    public interface GrantTypeEnum {
        /**
         * 授权码模式
         */
        public static final String AUTHORIZATION_CODE = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();
        /**
         * 隐式模式
         */
        public static final String IMPLICIT = "implicit";
        /**
         * 客户端凭证模式
         */
        public static final String CLIENT_CREDENTIALS = AuthorizationGrantType.CLIENT_CREDENTIALS.getValue();
        /**
         * 密码模式
         */
        public static final String PASSWORD = "password";
        /**
         * 刷新AccessToken
         */
        public static String REFRESH_TOKEN = AuthorizationGrantType.REFRESH_TOKEN.getValue();
        // --- 以上为 oauth2.0 的授权方式, 协议地址: https://datatracker.ietf.org/doc/html/rfc6749
        // --- oauth2.1 的授权协议中去掉了, 隐式模式, 和密码模式.
        public static final LinkedList<String> grantTypes = CollUtil.newLinkedList(AUTHORIZATION_CODE, IMPLICIT, CLIENT_CREDENTIALS, PASSWORD);

        public static final String AUTHORIZATION_CODE_enhance = AUTHORIZATION_CODE+ "_enhance";
        public static final String JWT_BEARER = AuthorizationGrantType.JWT_BEARER.getValue();
        public static final String DEVICE_CODE = AuthorizationGrantType.DEVICE_CODE.getValue();
        public static final String TOKEN_EXCHANGE = AuthorizationGrantType.TOKEN_EXCHANGE.getValue();
    }

}
