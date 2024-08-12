package com.authorization.utils.security;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-12 13:20
 */
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Autowired
    private SsoSecurityProperties ssoSecurityProperties;

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


}
