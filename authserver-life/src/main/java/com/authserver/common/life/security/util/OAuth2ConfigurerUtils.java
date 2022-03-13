//package com.authserver.common.life.security.util;
//
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import org.springframework.beans.factory.BeanFactoryUtils;
//import org.springframework.beans.factory.NoSuchBeanDefinitionException;
//import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.ResolvableType;
//import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.NimbusJwsEncoder;
//import org.springframework.security.oauth2.server.authorization.*;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.util.StringUtils;
//
//import java.util.Map;
//
///**
// * OAuth2配置工具类
// */
//public class OAuth2ConfigurerUtils {
//
//    public static <B extends HttpSecurityBuilder<B>> RegisteredClientRepository getRegisteredClientRepository(B builder) {
//        RegisteredClientRepository registeredClientRepository = builder.getSharedObject(RegisteredClientRepository.class);
//        if (registeredClientRepository == null) {
//            registeredClientRepository = getBean(builder, RegisteredClientRepository.class);
//            builder.setSharedObject(RegisteredClientRepository.class, registeredClientRepository);
//        }
//        return registeredClientRepository;
//    }
//
//    public static <B extends HttpSecurityBuilder<B>> OAuth2AuthorizationService getAuthorizationService(B builder) {
//        OAuth2AuthorizationService authorizationService = builder.getSharedObject(OAuth2AuthorizationService.class);
//        if (authorizationService == null) {
//            authorizationService = getOptionalBean(builder, OAuth2AuthorizationService.class);
//            if (authorizationService == null) {
//                authorizationService = new InMemoryOAuth2AuthorizationService();
//            }
//            builder.setSharedObject(OAuth2AuthorizationService.class, authorizationService);
//        }
//        return authorizationService;
//    }
//
//    public static <B extends HttpSecurityBuilder<B>> OAuth2AuthorizationConsentService getAuthorizationConsentService(B builder) {
//        OAuth2AuthorizationConsentService authorizationConsentService = builder.getSharedObject(OAuth2AuthorizationConsentService.class);
//        if (authorizationConsentService == null) {
//            authorizationConsentService = getOptionalBean(builder, OAuth2AuthorizationConsentService.class);
//            if (authorizationConsentService == null) {
//                authorizationConsentService = new InMemoryOAuth2AuthorizationConsentService();
//            }
//            builder.setSharedObject(OAuth2AuthorizationConsentService.class, authorizationConsentService);
//        }
//        return authorizationConsentService;
//    }
//
//    public static <B extends HttpSecurityBuilder<B>> JwtEncoder getJwtEncoder(B builder) {
//        JwtEncoder jwtEncoder = builder.getSharedObject(JwtEncoder.class);
//        if (jwtEncoder == null) {
//            jwtEncoder = getOptionalBean(builder, JwtEncoder.class);
//            if (jwtEncoder == null) {
//                JWKSource<SecurityContext> jwkSource = getJwkSource(builder);
//                jwtEncoder = new NimbusJwsEncoder(jwkSource);
//            }
//            builder.setSharedObject(JwtEncoder.class, jwtEncoder);
//        }
//        return jwtEncoder;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <B extends HttpSecurityBuilder<B>> JWKSource<SecurityContext> getJwkSource(B builder) {
//        JWKSource<SecurityContext> jwkSource = builder.getSharedObject(JWKSource.class);
//        if (jwkSource == null) {
//            ResolvableType type = ResolvableType.forClassWithGenerics(JWKSource.class, SecurityContext.class);
//            jwkSource = getBean(builder, type);
//            builder.setSharedObject(JWKSource.class, jwkSource);
//        }
//        return jwkSource;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <B extends HttpSecurityBuilder<B>> OAuth2TokenCustomizer<JwtEncodingContext> getJwtCustomizer(B builder) {
//        OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = builder.getSharedObject(OAuth2TokenCustomizer.class);
//        if (jwtCustomizer == null) {
//            ResolvableType type = ResolvableType.forClassWithGenerics(OAuth2TokenCustomizer.class, JwtEncodingContext.class);
//            jwtCustomizer = getOptionalBean(builder, type);
//            if (jwtCustomizer != null) {
//                builder.setSharedObject(OAuth2TokenCustomizer.class, jwtCustomizer);
//            }
//        }
//        return jwtCustomizer;
//    }
//
//    public static <B extends HttpSecurityBuilder<B>> ProviderSettings getProviderSettings(B builder) {
//        ProviderSettings providerSettings = builder.getSharedObject(ProviderSettings.class);
//        if (providerSettings == null) {
//            providerSettings = getOptionalBean(builder, ProviderSettings.class);
//            if (providerSettings == null) {
//                providerSettings = ProviderSettings.builder().build();
//            }
//            builder.setSharedObject(ProviderSettings.class, providerSettings);
//        }
//        return providerSettings;
//    }
//
//    public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, Class<T> type) {
//        return builder.getSharedObject(ApplicationContext.class).getBean(type);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, ResolvableType type) {
//        ApplicationContext context = builder.getSharedObject(ApplicationContext.class);
//        String[] names = context.getBeanNamesForType(type);
//        if (names.length == 1) {
//            return (T) context.getBean(names[0]);
//        }
//        if (names.length > 1) {
//            throw new NoUniqueBeanDefinitionException(type, names);
//        }
//        throw new NoSuchBeanDefinitionException(type);
//    }
//
//    public static <B extends HttpSecurityBuilder<B>, T> T getOptionalBean(B builder, Class<T> type) {
//        Map<String, T> beansMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
//                builder.getSharedObject(ApplicationContext.class), type);
//        if (beansMap.size() > 1) {
//            throw new NoUniqueBeanDefinitionException(type, beansMap.size(),
//                    "Expected single matching bean of type '" + type.getName() + "' but found " +
//                            beansMap.size() + ": " + StringUtils.collectionToCommaDelimitedString(beansMap.keySet()));
//        }
//        return (!beansMap.isEmpty() ? beansMap.values().iterator().next() : null);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <B extends HttpSecurityBuilder<B>, T> T getOptionalBean(B builder, ResolvableType type) {
//        ApplicationContext context = builder.getSharedObject(ApplicationContext.class);
//        String[] names = context.getBeanNamesForType(type);
//        if (names.length > 1) {
//            throw new NoUniqueBeanDefinitionException(type, names);
//        }
//        return names.length == 1 ? (T) context.getBean(names[0]) : null;
//    }
//
//}
