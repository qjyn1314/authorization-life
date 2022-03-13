//package com.authserver.common.life.security.util;
//
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.time.Instant;
//import java.util.Collections;
//import java.util.Set;
//
///**
// * jwt工具类
// */
//public final class JwtUtils {
//
//    private JwtUtils() {
//    }
//
////    public static JoseHeader.Builder headers() {
////        return JoseHeader.withAlgorithm(SignatureAlgorithm.RS256);
////    }
//
//    public static JwtClaimsSet.Builder accessTokenClaims(RegisteredClient registeredClient,
//                                                  String issuer, String subject, Set<String> authorizedScopes) {
//
//        Instant issuedAt = Instant.now();
//        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());
//
//        // @formatter:off
//        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
//        if (StringUtils.hasText(issuer)) {
//            claimsBuilder.issuer(issuer);
//        }
//        claimsBuilder
//                .subject(subject)
//                .audience(Collections.singletonList(registeredClient.getClientId()))
//                .issuedAt(issuedAt)
//                .expiresAt(expiresAt)
//                .notBefore(issuedAt);
//        if (!CollectionUtils.isEmpty(authorizedScopes)) {
//            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, authorizedScopes);
//        }
//        // @formatter:on
//
//        return claimsBuilder;
//    }
//}
