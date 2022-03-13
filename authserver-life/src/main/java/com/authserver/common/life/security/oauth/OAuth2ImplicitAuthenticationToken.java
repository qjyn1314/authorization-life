//package com.authserver.common.life.security.oauth;
//
//import org.springframework.lang.NonNull;
//import org.springframework.lang.Nullable;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.Version;
//import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
//import org.springframework.util.Assert;
//import org.springframework.util.CollectionUtils;
//
//import java.io.Serializable;
//import java.util.*;
//
//public class OAuth2ImplicitAuthenticationToken extends AbstractAuthenticationToken {
//    private static final long serialVersionUID = Version.SERIAL_VERSION_UID;
//    private String authorizationUri;
//    private String clientId;
//    private Authentication principal;
//    private String redirectUri;
//    private Set<String> scopes;
//    private String state;
//    private Map<String, Object> additionalParameters;
//
//    private OAuth2ImplicitAuthenticationToken() {
//        super(Collections.emptyList());
//    }
//
//    @Override
//    public Object getPrincipal() {
//        return this.principal;
//    }
//
//    @Override
//    public Object getCredentials() {
//        return "";
//    }
//
//    /**
//     * Returns the authorization URI.
//     *
//     * @return the authorization URI
//     */
//    public String getAuthorizationUri() {
//        return this.authorizationUri;
//    }
//
//    /**
//     * Returns the client identifier.
//     *
//     * @return the client identifier
//     */
//    public String getClientId() {
//        return this.clientId;
//    }
//
//    /**
//     * Returns the redirect uri.
//     *
//     * @return the redirect uri
//     */
//    @Nullable
//    public String getRedirectUri() {
//        return this.redirectUri;
//    }
//
//    /**
//     * Returns the requested (or authorized) scope(s).
//     *
//     * @return the requested (or authorized) scope(s), or an empty {@code Set} if not available
//     */
//    public Set<String> getScopes() {
//        return this.scopes;
//    }
//
//    /**
//     * Returns the state.
//     *
//     * @return the state
//     */
//    @Nullable
//    public String getState() {
//        return this.state;
//    }
//
//    /**
//     * Returns the additional parameters.
//     *
//     * @return the additional parameters
//     */
//    public Map<String, Object> getAdditionalParameters() {
//        return this.additionalParameters;
//    }
//
//    /**
//     * Returns a new {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}, initialized with the given client identifier
//     * and {@code Principal} (Resource Owner).
//     *
//     * @param clientId the client identifier
//     * @param principal the {@code Principal} (Resource Owner)
//     * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//     */
//    public static Builder with(@NonNull String clientId, @NonNull Authentication principal) {
//        Assert.hasText(clientId, "clientId cannot be empty");
//        Assert.notNull(principal, "principal cannot be null");
//        return new Builder(clientId, principal);
//    }
//
//    /**
//     * A builder for {@link OAuth2AuthorizationCodeRequestAuthenticationToken}.
//     */
//    public static final class Builder implements Serializable {
//        private static final long serialVersionUID = Version.SERIAL_VERSION_UID;
//        private String authorizationUri;
//        private String clientId;
//        private Authentication principal;
//        private String redirectUri;
//        private Set<String> scopes;
//        private String state;
//        private Map<String, Object> additionalParameters;
//
//        private Builder(String clientId, Authentication principal) {
//            this.clientId = clientId;
//            this.principal = principal;
//        }
//
//        /**
//         * Sets the authorization URI.
//         *
//         * @param authorizationUri the authorization URI
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//         */
//        public Builder authorizationUri(String authorizationUri) {
//            this.authorizationUri = authorizationUri;
//            return this;
//        }
//
//        /**
//         * Sets the redirect uri.
//         *
//         * @param redirectUri the redirect uri
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//         */
//        public Builder redirectUri(String redirectUri) {
//            this.redirectUri = redirectUri;
//            return this;
//        }
//
//        /**
//         * Sets the requested (or authorized) scope(s).
//         *
//         * @param scopes the requested (or authorized) scope(s)
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//         */
//        public Builder scopes(Set<String> scopes) {
//            if (scopes != null) {
//                this.scopes = new HashSet<>(scopes);
//            }
//            return this;
//        }
//
//        /**
//         * Sets the state.
//         *
//         * @param state the state
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//         */
//        public Builder state(String state) {
//            this.state = state;
//            return this;
//        }
//
//        /**
//         * Sets the additional parameters.
//         *
//         * @param additionalParameters the additional parameters
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken.Builder}
//         */
//        public Builder additionalParameters(Map<String, Object> additionalParameters) {
//            if (additionalParameters != null) {
//                this.additionalParameters = new HashMap<>(additionalParameters);
//            }
//            return this;
//        }
//
//        /**
//         * Builds a new {@link OAuth2AuthorizationCodeRequestAuthenticationToken}.
//         *
//         * @return the {@link OAuth2AuthorizationCodeRequestAuthenticationToken}
//         */
//        public OAuth2ImplicitAuthenticationToken build() {
//            Assert.hasText(this.authorizationUri, "authorizationUri cannot be empty");
//
//            OAuth2ImplicitAuthenticationToken authentication =
//                    new OAuth2ImplicitAuthenticationToken();
//
//            authentication.authorizationUri = this.authorizationUri;
//            authentication.clientId = this.clientId;
//            authentication.principal = this.principal;
//            authentication.redirectUri = this.redirectUri;
//            authentication.scopes = Collections.unmodifiableSet(
//                    !CollectionUtils.isEmpty(this.scopes) ?
//                            this.scopes :
//                            Collections.emptySet());
//            authentication.state = this.state;
//            authentication.additionalParameters = Collections.unmodifiableMap(
//                    !CollectionUtils.isEmpty(this.additionalParameters) ?
//                            this.additionalParameters :
//                            Collections.emptyMap());
//            authentication.setAuthenticated(true);
//
//            return authentication;
//        }
//
//    }
//}
