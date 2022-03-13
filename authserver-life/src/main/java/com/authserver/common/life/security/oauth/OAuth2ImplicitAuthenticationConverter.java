//package com.authserver.common.life.security.oauth;
//
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType;
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
//import org.springframework.security.web.authentication.AuthenticationConverter;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//public class OAuth2ImplicitAuthenticationConverter implements AuthenticationConverter {
//
//    private static final String DEFAULT_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";
//    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
//            "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
//
//    @Override
//    public Authentication convert(HttpServletRequest request) {
//        MultiValueMap<String, String> parameters = getParameters(request);
//
//        boolean authorizationRequest = false;
//        if ("GET".equals(request.getMethod())) {
//            authorizationRequest = true;
//
//            // response_type (REQUIRED)
//            String responseType = request.getParameter(OAuth2ParameterNames.RESPONSE_TYPE);
//            if (!StringUtils.hasText(responseType) ||
//                    parameters.get(OAuth2ParameterNames.RESPONSE_TYPE).size() != 1) {
//                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.RESPONSE_TYPE);
//            } else if (!responseType.equals(OAuth2AuthorizationResponseType.CODE.getValue())) {
//                // 走授权码模式
//                return null;
//            }
//        }
//
//        String authorizationUri = request.getRequestURL().toString();
//
//        // client_id (REQUIRED)
//        String clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
//        if (!StringUtils.hasText(clientId) ||
//                parameters.get(OAuth2ParameterNames.CLIENT_ID).size() != 1) {
//            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CLIENT_ID);
//        }
//
//        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
//        if (principal == null) {
//            principal = ANONYMOUS_AUTHENTICATION;
//        }
//
//        // redirect_uri (OPTIONAL)
//        String redirectUri = parameters.getFirst(OAuth2ParameterNames.REDIRECT_URI);
//        if (StringUtils.hasText(redirectUri) &&
//                parameters.get(OAuth2ParameterNames.REDIRECT_URI).size() != 1) {
//            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.REDIRECT_URI);
//        }
//
//        // scope (OPTIONAL)
//        Set<String> scopes = null;
//        if (authorizationRequest) {
//            String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
//            if (StringUtils.hasText(scope) &&
//                    parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
//                throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE);
//            }
//            if (StringUtils.hasText(scope)) {
//                scopes = new HashSet<>(
//                        Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
//            }
//        } else {
//            // Consent request
//            if (parameters.containsKey(OAuth2ParameterNames.SCOPE)) {
//                scopes = new HashSet<>(parameters.get(OAuth2ParameterNames.SCOPE));
//            }
//        }
//
//        // state (RECOMMENDED)
//        String state = parameters.getFirst(OAuth2ParameterNames.STATE);
//        if (StringUtils.hasText(state) &&
//                parameters.get(OAuth2ParameterNames.STATE).size() != 1) {
//            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.STATE);
//        }
//
//        Map<String, Object> additionalParameters = new HashMap<>();
//        parameters.forEach((key, value) -> {
//            if (!key.equals(OAuth2ParameterNames.CLIENT_ID) &&
//                    !key.equals(OAuth2ParameterNames.REDIRECT_URI) &&
//                    !key.equals(OAuth2ParameterNames.SCOPE) &&
//                    !key.equals(OAuth2ParameterNames.STATE)) {
//                additionalParameters.put(key, value.get(0));
//            }
//        });
//        return OAuth2AuthorizationCodeRequestAuthenticationToken.with(clientId, principal)
//                .authorizationUri(authorizationUri)
//                .redirectUri(redirectUri)
//                .scopes(scopes)
//                .state(state)
//                .additionalParameters(additionalParameters)
//                .consent(!authorizationRequest)
//                .build();
//    }
//
//    static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
//        parameterMap.forEach((key, values) -> {
//            if (values.length > 0) {
//                for (String value : values) {
//                    parameters.add(key, value);
//                }
//            }
//        });
//        return parameters;
//    }
//
//    private static void throwError(String errorCode, String parameterName) {
//        throwError(errorCode, parameterName, DEFAULT_ERROR_URI);
//    }
//
//    private static void throwError(String errorCode, String parameterName, String errorUri) {
//        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
//        throw new OAuth2AuthorizationCodeRequestAuthenticationException(error, null);
//    }
//}
