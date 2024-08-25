package com.authorization.utils.security;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-12 13:20
 */
@Slf4j
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Autowired
    private SsoSecurityProperties ssoSecurityProperties;

    @Override
    public String createJwtToken(Map<String, Object> claims) {
        return createJwtToken(ssoSecurityProperties.getSecret(), claims);
    }

    @Override
    public String createJwtToken(String secret, Map<String, Object> claims) {
        Assert.notEmpty(claims, "claims must not be empty");
        if (StrUtil.isBlank(secret)) {
            secret = ssoSecurityProperties.getSecret();
            log.info("使用的secret是->{}", secret);
        }
        // 此处保证每次生成的JwtToken不一致
        claims.put("uuid", UUID.fastUUID().toString(true));
        claims.put("iss", SsoSecurityProperties.DEFAULT_ISSUER);
        // 过期时间戳, 默认过期24小时
        long epochSecond = LocalDateTime.now().plusSeconds(SsoSecurityProperties.ACCESS_TOKEN_TIME_TO_LIVE).toEpochSecond(ZoneOffset.UTC);
        claims.put("exp", epochSecond);
        // JWT的第一部分: Header,包含令牌的类型(JWT)和使用的签名算法(如HMAC SHA256或RSA)。
        // JWE和JWS 的区别: JWS和JWE都是为了增强JSON数据的安全性，‌但侧重点不同。‌JWS侧重于保证数据的完整性和不被篡改，‌而JWE则在保证数据完整性的基础上，‌进一步保证了数据的保密性。‌
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
        // JWT的第二部分: Payload, 是有效负载
        Payload payload = new Payload(claims);
        // JWT的第三部分: Signature, 创建签名部分，您必须使用已编码的报头、已编码的有效载荷、一个秘密、报头中指定的算法，并对其进行签名。
        // HMAC算法以及工作原理: https://blog.csdn.net/wdxabc1/article/details/137993010
        JWSSigner jwsSigner = null;
        try {
            jwsSigner = new MACSigner(secret);
        } catch (Exception e) {
            log.error("创建签名失败", e);
            return null;
        }
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(jwsSigner);
        } catch (Exception e) {
            log.error("对数据签名失败", e);
            return null;
        }
        return jwsObject.serialize();
    }

    @Override
    public boolean validateJwtToken(String jwtToken) {
        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(ssoSecurityProperties.getSecret());
        } catch (Exception e) {
            log.error("验证签名创建失败", e);
            return false;
        }
        JWSObject object = null;
        try {
            object = JWSObject.parse(jwtToken);
        } catch (Exception e) {
            log.error("反编译jwtToken失败", e);
            return false;
        }
        try {
            boolean verify = object.verify(verifier);
            if (verify) {
                boolean exped = Long.parseLong(object.getPayload().toJSONObject().get("exp").toString()) >= LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                log.warn("{}:jwtToken已过期...", jwtToken);
                return exped;
            }
            return false;
        } catch (Exception e) {
            log.error("验证jwtToken失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getClaimsFromJwtToken(String jwtToken) {
        JWSObject object = null;
        try {
            object = JWSObject.parse(jwtToken);
        } catch (Exception e) {
            log.error("反编译jwtToken失败", e);
            return null;
        }
        return validateJwtToken(jwtToken) ? object.getPayload().toJSONObject() : null;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource jwkSource) {
        // 直接粘贴的类: org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration
        Set<JWSAlgorithm> jwsAlgs = new HashSet<>();
        jwsAlgs.addAll(JWSAlgorithm.Family.RSA);
        jwsAlgs.addAll(JWSAlgorithm.Family.EC);
        jwsAlgs.addAll(JWSAlgorithm.Family.HMAC_SHA);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgs, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        // Override the default Nimbus claims set verifier as NimbusJwtDecoder handles it
        // instead
        jwtProcessor.setJWTClaimsSetVerifier((claims, context) -> {
        });
        return new NimbusJwtDecoder(jwtProcessor);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 直接粘贴的类: org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration
        RSAKey rsaKey = getRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static RSAKey getRsaKey() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey)
                .keyID(java.util.UUID.randomUUID().toString())
                .build();
        return rsaKey;
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

}
