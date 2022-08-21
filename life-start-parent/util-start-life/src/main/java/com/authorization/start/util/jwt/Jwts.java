package com.authorization.start.util.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

/**
 * jwt工具类
 */
@Slf4j
public class Jwts {

    public static final String HEADER_JWT = "Jwt-Token";

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";

    /**
     * 默认jwt sercet, 如果要使用此当以sercet, 密码不得低于32位
     */
    public static final String DEFAULT_SECRET = "3mvVp3~MrWae_r1sen36j93u_zcFJsMf";
    public static final String SECRET_EXPRESS = "${auth-server.jwt.secret:" + DEFAULT_SECRET + "}";

    /**
     * 创建jwt头，默认hs256算法，类型为jwt
     *
     * @return JWSHeader
     */
    public static JWSHeader header() {
        return new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
    }

    /**
     * 创建mac签名器，因允许外部配置，因此需要配合{@link Jwts#SECRET_EXPRESS}使用
     *
     * @param secret 签名密钥
     * @return JWSSigner
     */
    public static JWSSigner signer(String secret) {
        try {
            return new MACSigner(secret);
        } catch (KeyLengthException e) {
            log.error("JWS MACSigner couldn't created! secret: [{}]", secret);
            throw new JwtException(e);
        }
    }

    /**
     * 创建mac校验器，因允许外部配置，因此需要配合{@link Jwts#SECRET_EXPRESS}使用
     *
     * @param secret 签名密钥
     * @return JWSVerifier
     */
    public static JWSVerifier verifier(String secret) {
        try {
            return new MACVerifier(secret);
        } catch (JOSEException e) {
            log.error("JWS MACVerifier couldn't created! secret: [{}]", secret);
            throw new JwtException(e);
        }
    }

    /**
     * 进行jwt签名
     *
     * @param jwsObject jwt
     * @param jwsSigner 签名器
     * @return 返回对象为签名后的原始对象
     */
    public static JWSObject sign(JWSObject jwsObject, JWSSigner jwsSigner) {
        try {
            jwsObject.sign(jwsSigner);
            return jwsObject;
        } catch (JOSEException e) {
            log.error("JWT 签名失败, 请检查! payload: [{}]", jwsObject.getPayload());
            throw new JwtException(e);
        }
    }

    /**
     * 进行jwt签名并序列化
     *
     * @param jwsObject jwt
     * @param jwsSigner 签名器
     * @return 序列化的jwt
     */
    public static String signAndSerialize(JWSObject jwsObject, JWSSigner jwsSigner) {
        return sign(jwsObject, jwsSigner).serialize();
    }

    /**
     * 将jwt反序列化为JWSObject
     *
     * @param jwtToken jwt字符串
     * @return JWSObject
     */
    public static JWSObject parse(String jwtToken) {
        try {
            return JWSObject.parse(jwtToken);
        } catch (ParseException e) {
            log.error("Jwt parse failed! JWT: [{}]", jwtToken);
            throw new JwtException(e);
        }
    }

    /**
     * 进行jwt验证
     *
     * @param jwtToken    序列化后的jwt
     * @param jwsVerifier jwt验证器
     */
    public static boolean verify(String jwtToken, JWSVerifier jwsVerifier) {
        try {
            JWSObject jwsObject = parse(jwtToken);
            return jwsObject.verify(jwsVerifier);
        } catch (JOSEException e) {
            log.error("Jwt couldn't be verified! JWT: [{}]", jwtToken);
            throw new JwtException(e);
        }
    }

    /**
     * 进行jwt验证
     *
     * @param jwsObject   jwsObject
     * @param jwsVerifier jwt验证器
     */
    public static boolean verify(JWSObject jwsObject, JWSVerifier jwsVerifier) {
        try {
            return jwsObject.verify(jwsVerifier);
        } catch (JOSEException e) {
            log.error("Jwt couldn't be verified! JWSObject: [{}]", jwsObject);
            throw new JwtException(e);
        }
    }
}
