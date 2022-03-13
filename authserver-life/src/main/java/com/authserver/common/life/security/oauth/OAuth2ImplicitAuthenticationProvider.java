//package com.authserver.common.life.security.oauth;
//
//import com.authserver.common.life.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
//import org.springframework.security.crypto.keygen.StringKeyGenerator;
//import org.springframework.security.oauth2.core.*;
//import org.springframework.security.oauth2.core.authentication.OAuth2AuthenticationContext;
//import org.springframework.security.oauth2.core.authentication.OAuth2AuthenticationValidator;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
//import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
//import org.springframework.security.oauth2.server.authorization.authentication.*;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//
//import java.security.Principal;
//import java.time.Duration;
//import java.time.Instant;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//public class OAuth2ImplicitAuthenticationProvider implements AuthenticationProvider {
//
//    private final RegisteredClientRepository registeredClientRepository;
//    private final OAuth2AuthorizationService authorizationService;
//    private final JwtEncoder jwtEncoder;
//    private ProviderSettings providerSettings;
//
//    private static final StringKeyGenerator DEFAULT_REFRESH_TOKEN_GENERATOR =
//            new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
//    private OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer = (context) -> {};
//    private Supplier<String> refreshTokenGenerator = DEFAULT_REFRESH_TOKEN_GENERATOR::generateKey;
//    private static final Function<String, OAuth2AuthenticationValidator> DEFAULT_AUTHENTICATION_VALIDATOR_RESOLVER =
//            createDefaultAuthenticationValidatorResolver();
//    private Function<String, OAuth2AuthenticationValidator> authenticationValidatorResolver = DEFAULT_AUTHENTICATION_VALIDATOR_RESOLVER;
//
//    public OAuth2ImplicitAuthenticationProvider(OAuth2AuthorizationService authorizationService,
//                                                JwtEncoder jwtEncoder,
//                                                RegisteredClientRepository registeredClientRepository) {
//        Assert.notNull(authorizationService, "authorizationService cannot be null");
//        Assert.notNull(jwtEncoder, "jwtEncoder cannot be null");
//        this.authorizationService = authorizationService;
//        this.jwtEncoder = jwtEncoder;
//        this.registeredClientRepository = registeredClientRepository;
//
//    }
//
//    @Autowired(required = false)
//    protected void setProviderSettings(ProviderSettings providerSettings) {
//        this.providerSettings = providerSettings;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
//                (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
//        if (!Objects.equals(authorizationCodeRequestAuthentication.getAdditionalParameters()
//                .get(OAuth2ParameterNames.RESPONSE_TYPE), OAuth2AuthorizationResponseType.TOKEN.getValue())){
//            return null;
//        }
//
//        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(
//                authorizationCodeRequestAuthentication.getClientId());
//        if (registeredClient == null) {
//            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CLIENT_ID,
//                    authorizationCodeRequestAuthentication, null);
//        }
//
//        Map<Object, Object> context = new HashMap<>();
//        context.put(RegisteredClient.class, registeredClient);
//        OAuth2AuthenticationContext authenticationContext = new OAuth2AuthenticationContext(
//                authorizationCodeRequestAuthentication, context);
//
//        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.AUTHORIZATION_CODE)) {
//            throwError(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, OAuth2ParameterNames.CLIENT_ID,
//                    authorizationCodeRequestAuthentication, registeredClient);
//        }
//
//        OAuth2AuthenticationValidator scopeValidator = resolveAuthenticationValidator(OAuth2ParameterNames.SCOPE);
//        scopeValidator.validate(authenticationContext);
//
//        // ---------------
//        // The request is valid - ensure the resource owner is authenticated
//        // ---------------
//
//        Authentication principal = (Authentication) authorizationCodeRequestAuthentication.getPrincipal();
//        if (!isPrincipalAuthenticated(principal)) {
//            // Return the authorization request as-is where isAuthenticated() is false
//            return authorizationCodeRequestAuthentication;
//        }
//
//        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
//                .authorizationUri(authorizationCodeRequestAuthentication.getAuthorizationUri())
//                .clientId(registeredClient.getClientId())
//                .redirectUri(authorizationCodeRequestAuthentication.getRedirectUri())
//                .scopes(authorizationCodeRequestAuthentication.getScopes())
//                .state(authorizationCodeRequestAuthentication.getState())
//                .additionalParameters(authorizationCodeRequestAuthentication.getAdditionalParameters())
//                .build();
//
//        OAuth2Authorization authorization = authorizationBuilder(registeredClient, principal, authorizationRequest)
//                .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizationRequest.getScopes())
//                .build();
//
//        // 认证
//        String issuer = this.providerSettings != null ? this.providerSettings.getIssuer() : null;
//        Set<String> authorizedScopes = authorization.getAttribute(
//                OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME);
//
//        JoseHeader.Builder headersBuilder = JwtUtils.headers();
//        JwtClaimsSet.Builder claimsBuilder = JwtUtils.accessTokenClaims(
//                registeredClient, issuer, authorization.getPrincipalName(),
//                authorizedScopes);
//
//        // @formatter:off
//        JwtEncodingContext jwtContext = JwtEncodingContext.with(headersBuilder, claimsBuilder)
//                .registeredClient(registeredClient)
//                .principal(authorization.getAttribute(Principal.class.getName()))
//                .authorization(authorization)
//                .authorizedScopes(authorizedScopes)
//                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrant(authorizationCodeRequestAuthentication)
//                .build();
//        // @formatter:on
//
//        this.jwtCustomizer.customize(jwtContext);
//
//        JoseHeader headers = jwtContext.getHeaders().build();
//        JwtClaimsSet claims = jwtContext.getClaims().build();
//        Jwt jwtAccessToken = this.jwtEncoder.encode(headers, claims);
//
//        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
//                jwtAccessToken.getTokenValue(), jwtAccessToken.getIssuedAt(),
//                jwtAccessToken.getExpiresAt(), authorizedScopes);
//
//        OAuth2RefreshToken refreshToken = null;
//        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
//            refreshToken = generateRefreshToken(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
//        }
//
//        // @formatter:off
//        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
//                .token(accessToken,
//                        (metadata) ->
//                                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, jwtAccessToken.getClaims())
//                );
//        if (refreshToken != null) {
//            authorizationBuilder.refreshToken(refreshToken);
//        }
//        // @formatter:on
//
//        Map<String, Object> additionalParameters = Collections.emptyMap();
//        // @formatter:off
//        authorization = authorizationBuilder.build();
//        // @formatter:on
//
//        this.authorizationService.save(authorization);
//        OAuth2ClientAuthenticationToken clientAuthenticationToken = new OAuth2ClientAuthenticationToken(registeredClient, ClientAuthenticationMethod.NONE, null);
//        OAuth2AuthorizationCodeRequestAuthenticationToken result =
//                OAuth2AuthorizationCodeRequestAuthenticationToken.with(authorizationCodeRequestAuthentication.getClientId(),
//                // 隐藏起来
//                new OAuth2AccessTokenAuthenticationToken(
//                        registeredClient, clientAuthenticationToken, accessToken, refreshToken, additionalParameters))
//                .consent(false)
//                .consentRequired(false)
//                .authorizationUri(authorizationCodeRequestAuthentication.getAuthorizationUri())
//                        .state(authorizationCodeRequestAuthentication.getState())
//                        .redirectUri(Objects.nonNull(authorizationCodeRequestAuthentication.getRedirectUri()) ? authorizationCodeRequestAuthentication.getRedirectUri()
//                                : registeredClient.getRedirectUris().stream().findFirst().orElse("buyer.zhichubao.com"))
//                .build();
//        result.setAuthenticated(true);
//        return result;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return OAuth2AuthorizationCodeRequestAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//
//    private static boolean isPrincipalAuthenticated(Authentication principal) {
//        return principal != null &&
//                !AnonymousAuthenticationToken.class.isAssignableFrom(principal.getClass()) &&
//                principal.isAuthenticated();
//    }
//
//    private OAuth2AuthenticationValidator resolveAuthenticationValidator(String parameterName) {
//        OAuth2AuthenticationValidator authenticationValidator = this.authenticationValidatorResolver.apply(parameterName);
//        return authenticationValidator != null ?
//                authenticationValidator :
//                DEFAULT_AUTHENTICATION_VALIDATOR_RESOLVER.apply(parameterName);
//    }
//
//    private static void throwError(String errorCode, String parameterName,
//                                   OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication,
//                                   RegisteredClient registeredClient) {
//        throwError(errorCode, parameterName, authorizationCodeRequestAuthentication, registeredClient, null);
//    }
//
//    private static void throwError(String errorCode, String parameterName,
//                                   OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication,
//                                   RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
//        throwError(errorCode, parameterName, "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1",
//                authorizationCodeRequestAuthentication, registeredClient, authorizationRequest);
//    }
//
//    private static void throwError(String errorCode, String parameterName, String errorUri,
//                                   OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication,
//                                   RegisteredClient registeredClient, OAuth2AuthorizationRequest authorizationRequest) {
//
//        boolean redirectOnError = true;
//        if (errorCode.equals(OAuth2ErrorCodes.INVALID_REQUEST) &&
//                (parameterName.equals(OAuth2ParameterNames.CLIENT_ID) ||
//                        parameterName.equals(OAuth2ParameterNames.REDIRECT_URI) ||
//                        parameterName.equals(OAuth2ParameterNames.STATE))) {
//            redirectOnError = false;
//        }
//
//        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthenticationResult = authorizationCodeRequestAuthentication;
//
//        if (redirectOnError && !StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
//            String redirectUri = resolveRedirectUri(authorizationRequest, registeredClient);
//            String state = authorizationCodeRequestAuthentication.isConsent() && authorizationRequest != null ?
//                    authorizationRequest.getState() : authorizationCodeRequestAuthentication.getState();
//            authorizationCodeRequestAuthenticationResult = from(authorizationCodeRequestAuthentication)
//                    .redirectUri(redirectUri)
//                    .state(state)
//                    .build();
//            authorizationCodeRequestAuthenticationResult.setAuthenticated(authorizationCodeRequestAuthentication.isAuthenticated());
//        } else if (!redirectOnError && StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())) {
//            authorizationCodeRequestAuthenticationResult = from(authorizationCodeRequestAuthentication)
//                    .redirectUri(null)		// Prevent redirects
//                    .build();
//            authorizationCodeRequestAuthenticationResult.setAuthenticated(authorizationCodeRequestAuthentication.isAuthenticated());
//        }
//
//        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
//        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, authorizationCodeRequestAuthenticationResult);
//    }
//
//    private static String resolveRedirectUri(OAuth2AuthorizationRequest authorizationRequest, RegisteredClient registeredClient) {
//        if (authorizationRequest != null && StringUtils.hasText(authorizationRequest.getRedirectUri())) {
//            return authorizationRequest.getRedirectUri();
//        }
//        if (registeredClient != null) {
//            return registeredClient.getRedirectUris().iterator().next();
//        }
//        return null;
//    }
//
//    private static OAuth2AuthorizationCodeRequestAuthenticationToken.Builder from(OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication) {
//        return OAuth2AuthorizationCodeRequestAuthenticationToken.with(authorizationCodeRequestAuthentication.getClientId(), (Authentication) authorizationCodeRequestAuthentication.getPrincipal())
//                .authorizationUri(authorizationCodeRequestAuthentication.getAuthorizationUri())
//                .redirectUri(authorizationCodeRequestAuthentication.getRedirectUri())
//                .scopes(authorizationCodeRequestAuthentication.getScopes())
//                .state(authorizationCodeRequestAuthentication.getState())
//                .additionalParameters(authorizationCodeRequestAuthentication.getAdditionalParameters())
//                .consentRequired(authorizationCodeRequestAuthentication.isConsentRequired())
//                .consent(authorizationCodeRequestAuthentication.isConsent())
//                .authorizationCode(authorizationCodeRequestAuthentication.getAuthorizationCode());
//    }
//
//    private static Function<String, OAuth2AuthenticationValidator> createDefaultAuthenticationValidatorResolver() {
//        Map<String, OAuth2AuthenticationValidator> authenticationValidators = new HashMap<>();
//        authenticationValidators.put(OAuth2ParameterNames.SCOPE, new DefaultScopeOAuth2AuthenticationValidator());
//        return authenticationValidators::get;
//    }
//
//    private static class DefaultScopeOAuth2AuthenticationValidator implements OAuth2AuthenticationValidator {
//
//        @Override
//        public void validate(OAuth2AuthenticationContext authenticationContext) {
//            OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication =
//                    authenticationContext.getAuthentication();
//            RegisteredClient registeredClient = authenticationContext.get(RegisteredClient.class);
//            Authentication principal = (Authentication) authorizationCodeRequestAuthentication.getPrincipal();
//
//            Set<String> requestedScopes = authorizationCodeRequestAuthentication.getScopes();
//            Assert.notNull(registeredClient, "未找到注册客户端。");
//            Set<String> allowedScopes = registeredClient.getScopes();
//            boolean express = !requestedScopes.isEmpty()
//                    && !allowedScopes.containsAll(requestedScopes);
//            if (principal.getPrincipal() instanceof User){
//                Set<String> userGroups = ((User) principal.getPrincipal()).getUserGroups();
//                express = express || !userGroups.containsAll(allowedScopes);
//            }
//
//            if (express) {
//                throwError(OAuth2ErrorCodes.INVALID_SCOPE, OAuth2ParameterNames.SCOPE,
//                        authorizationCodeRequestAuthentication, registeredClient);
//            }
//        }
//
//    }
//
//    private static OAuth2Authorization.Builder authorizationBuilder(RegisteredClient registeredClient, Authentication principal,
//                                                                    OAuth2AuthorizationRequest authorizationRequest) {
//        return OAuth2Authorization.withRegisteredClient(registeredClient)
//                .principalName(principal.getName())
//                .authorizationGrantType(AuthorizationGrantType.IMPLICIT)
//                .attribute(Principal.class.getName(), principal)
//                .attribute(OAuth2AuthorizationRequest.class.getName(), authorizationRequest);
//    }
//
//    /**
//     * Sets the {@link OAuth2TokenCustomizer} that customizes the
//     * {@link JwtEncodingContext.Builder#headers(Consumer) headers} and/or
//     * {@link JwtEncodingContext.Builder#claims(Consumer) claims} for the generated {@link Jwt}.
//     *
//     * @param jwtCustomizer the {@link OAuth2TokenCustomizer} that customizes the headers and/or claims for the generated {@code Jwt}
//     */
//    public void setJwtCustomizer(OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
//        Assert.notNull(jwtCustomizer, "jwtCustomizer cannot be null");
//        this.jwtCustomizer = jwtCustomizer;
//    }
//
//    /**
//     * Sets the {@code Supplier<String>} that generates the value for the {@link OAuth2RefreshToken}.
//     *
//     * @param refreshTokenGenerator the {@code Supplier<String>} that generates the value for the {@link OAuth2RefreshToken}
//     */
//    public void setRefreshTokenGenerator(Supplier<String> refreshTokenGenerator) {
//        Assert.notNull(refreshTokenGenerator, "refreshTokenGenerator cannot be null");
//        this.refreshTokenGenerator = refreshTokenGenerator;
//    }
//
//    private OAuth2RefreshToken generateRefreshToken(Duration tokenTimeToLive) {
//        Instant issuedAt = Instant.now();
//        Instant expiresAt = issuedAt.plus(tokenTimeToLive);
//        return new OAuth2RefreshToken(this.refreshTokenGenerator.get(), issuedAt, expiresAt);
//    }
//}
