package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.authorization.common.exception.handle.CommonException;
import com.authorization.core.proxy.CurrentProxy;
import com.authorization.life.auth.app.dto.OauthClientDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.AuthorizationGrant;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.OauthClient;
import com.authorization.life.auth.infra.mapper.OauthClientMapper;
import com.authorization.utils.security.SecurityCoreService;
import com.authorization.utils.security.SsoSecurityProperties;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * oauth客户端表
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Slf4j
@Service
public class OauthClientServiceImpl
        implements OauthClientService, CurrentProxy<OauthClientService> {

    @Autowired
    private OauthClientMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SsoSecurityProperties ssoSecurityProperties;

    @Override
    public OauthClientVO selectClientByClientId(String id) {
        Assert.notBlank(id, () -> new CommonException("clientId不能为空."));
        OauthClient oauthClient =
                mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId, id));
        Assert.notNull(oauthClient, () -> new CommonException("根据clientId未找到Client信息."));
        return Convert.convert(OauthClientVO.class, oauthClient);
    }

    /**
     * 根据 domainName 没有查询到client信息时, 则查询 根据默认域名(www.authorization.life)查询client信息
     *
     * @param domainName 登录或注册时使用的域名
     * @return com.authorization.life.auth.app.vo.OauthClientVO
     */
    @Override
    public OauthClientVO clientByDomain(String domainName) {
        Assert.notBlank(domainName, "域名不能为空. ");
        log.info("此次请求的域名是->{}", domainName);
        OauthClient oauthClient =
                mapper.selectOne(
                        Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, domainName));
        if (Objects.isNull(oauthClient)) {
            oauthClient =
                    mapper.selectOne(
                            Wrappers.lambdaQuery(OauthClient.class)
                                    .eq(OauthClient::getDomainName, SecurityCoreService.DEFAULT_DOMAIN));
        }
        Assert.notNull(oauthClient, "根据域名未找到client信息.");
        OauthClientVO oauthClientVO = Convert.convert(OauthClientVO.class, oauthClient);
        oauthClientVO.setClientSecret(oauthClient.getClientSecretBak());
        return oauthClientVO;
    }

    @Override
    public PageInfo<OauthClientVO> page(OauthClientDTO clientDTO) {
        Page<OauthClientVO> startPage =
                PageHelper.startPage(clientDTO.getPageNum(), clientDTO.getPageSize());
        return startPage.doSelectPageInfo(() -> clientList(clientDTO));
    }

    private List<OauthClientVO> clientList(OauthClientDTO clientDTO) {
        List<OauthClient> oauthClients = mapper.page(clientDTO);
        return oauthClients.stream()
                .map(oauthClient -> Convert.convert(OauthClientVO.class, oauthClient))
                .collect(Collectors.toList());
    }

    /**
     * 生成获取accessToken的URL
     *
     * @param hostOrigin 授权服务器域名前缀
     * @param grantType 授权模式
     * @return String
     */
    public static String genAuthorizationTokenUrl(
            String hostOrigin, AuthorizationGrantType grantType) {
        String url = "";
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(grantType)) {
            url = hostOrigin + UriComponentsBuilder.fromPath("/oauth2/token").toUriString();
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            url = hostOrigin + UriComponentsBuilder.fromPath("/oauth2/authorize").toUriString();
        }
        return url;
    }

    public static final String GET = "GET";
    public static final String POST = "POST";

    /**
     * 用于构建post请求的参数
     *
     * @param grantType 授权模式
     * @return map
     */
    public static Map<String, String> paramsMap(
            AuthorizationGrantType grantType, String redirectUri, String clientId, String clientSecret) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(grantType)) {
            Map<String, String> params = new HashMap<>(6);
            params.put(
                    OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
            params.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
            params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
            params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
            params.put(OAuth2ParameterNames.CODE, "URL中的临时CODE");
            params.put(OAuth2ParameterNames.STATE, "URL中的state");
            return params;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            Map<String, String> params = new HashMap<>(3);
            params.put(
                    OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
            params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
            params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
            return params;
        }

        return Map.of();
    }

    @Override
    public List<OauthClientVO> genAuthorizationUrl(String clientId, String hostOrigin) {
        OauthClientVO oauthClientVO = selectClientByClientId(clientId);
        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String redirectUri = oauthClientVO.getRedirectUri();
        String scopes = oauthClientVO.getScopes();
        String grantTypes = oauthClientVO.getGrantTypes();
        if (StrUtil.isBlank(redirectUri) || StrUtil.isBlank(scopes) || StrUtil.isBlank(grantTypes)) {
            return List.of();
        }

        Set<String> grantTypeSet =
                Arrays.stream(grantTypes.split(StrUtil.COMMA))
                        .sorted()
                        .collect(Collectors.toCollection(TreeSet::new));
        Set<String> scopeSet = Arrays.stream(scopes.split(StrUtil.COMMA)).collect(Collectors.toSet());
        Set<String> urlList =
                Arrays.stream(redirectUri.split(StrUtil.COMMA)).collect(Collectors.toSet());

        log.info("hostOrigin->{}", hostOrigin);
        Set<String> authUrlSet = new HashSet<>();
        List<OauthClientVO> clientUrls = CollUtil.newArrayList();

        for (String url : urlList) {
            for (String code : grantTypeSet) {
                for (String scope : scopeSet) {
                    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(code)) {
                        String state = UUID.fastUUID().toString(true);
                        String authUrl = genAuthorizationCodeUrl(hostOrigin, clientId, scope, state, url);
                        if (authUrlSet.contains(authUrl)) {
                            continue;
                        }
                        authUrlSet.add(authUrl);
                        OauthClientVO oauthClient = new OauthClientVO();
                        BeanUtils.copyProperties(oauthClientVO, oauthClient);
                        oauthClient.setGrantTypes(code);
                        oauthClient.setScopes(scope);
                        oauthClient.setMethod(GET);
                        oauthClient.setRedirectUrl(authUrl);
                        oauthClient.setRedirectUri(url);
                        clientUrls.add(oauthClient);

                        OauthClientVO oauthClient02 = new OauthClientVO();
                        BeanUtils.copyProperties(oauthClientVO, oauthClient02);
                        oauthClient02.setGrantTypes(code);
                        oauthClient02.setScopes(scope);
                        oauthClient02.setMethod(POST);
                        oauthClient02.setRedirectUrl(
                                genAuthorizationTokenUrl(hostOrigin, AuthorizationGrantType.AUTHORIZATION_CODE));
                        oauthClient02.setRedirectUri(url);
                        oauthClient02.setParams(
                                paramsMap(
                                        AuthorizationGrantType.AUTHORIZATION_CODE, url, clientId, clientSecretBak));
                        clientUrls.add(oauthClient02);

                    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(code)) {
                        OauthClientVO oauthClient02 = new OauthClientVO();
                        BeanUtils.copyProperties(oauthClientVO, oauthClient02);
                        oauthClient02.setGrantTypes(code);
                        oauthClient02.setMethod(POST);
                        oauthClient02.setRedirectUrl(
                                genAuthorizationTokenUrl(hostOrigin, AuthorizationGrantType.CLIENT_CREDENTIALS));
                        oauthClient02.setRedirectUri(url);
                        oauthClient02.setParams(
                                paramsMap(
                                        AuthorizationGrantType.CLIENT_CREDENTIALS, url, clientId, clientSecretBak));
                        clientUrls.add(oauthClient02);
                    }
                }
            }
        }

        return clientUrls;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OauthClientVO saveClient(OauthClientDTO clientDTO) {
        Assert.isFalse(
                "passport".equals(clientDTO.getClientId()),
                () -> new CommonException("\"passport\"为基本client, 不允许修改"));
        OauthClient oauthClient = Convert.convert(OauthClient.class, clientDTO);

        OauthClient existOauthClient =
                mapper.selectOne(
                        Wrappers.lambdaQuery(OauthClient.class)
                                .eq(OauthClient::getClientId, clientDTO.getClientId()));

        String encode = passwordEncoder.encode(oauthClient.getClientSecretBak());
        oauthClient.setClientSecret(encode);
        if (Objects.isNull(existOauthClient)) {
            mapper.insert(oauthClient);
        } else {
            String oauthClientId = existOauthClient.getOauthClientId();
            oauthClient.setOauthClientId(oauthClientId);
            mapper.updateById(oauthClient);
        }
        return Convert.convert(OauthClientVO.class, oauthClient);
    }

    @Override
    public OauthClientVO getClient(String clientId) {
        return selectClientByClientId(clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delClient(String clientId) {
        Assert.isFalse(
                "passport".equals(clientId), () -> new CommonException("\"passport\"为基本client, 不允许删除"));
        OauthClientVO oauthClientVO = selectClientByClientId(clientId);
        Assert.notNull(oauthClientVO);
        LambdaQueryWrapper<OauthClient> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OauthClient::getClientId, clientId);
        int delete = mapper.delete(wrapper);
        return delete > 0;
    }

    @Override
    public List<AuthorizationGrant> genGrantTypeUrl(String clientId) {
        OauthClientVO oauthClientVO = selectClientByClientId(clientId);
        LinkedList<AuthorizationGrant> authorizationGrantList = Lists.newLinkedList();
        for (Map.Entry<String, String> grantTypeMap :
                AuthorizationGrant.GrantTypeEnum.GRANT_TYPES.entrySet()) {
            String key = grantTypeMap.getKey();
            String value = grantTypeMap.getValue();
            AuthorizationGrant authorizationGrant = new AuthorizationGrant();
            authorizationGrantList.add(authorizationGrant);
            authorizationGrant.setClientId(clientId);
            authorizationGrant.setGrantType(key);
            authorizationGrant.setGrantTypeContent(value);
            genGrantUrl(key, oauthClientVO, authorizationGrant);
        }
        for (Map.Entry<String, String> grantTypeMap :
                AuthorizationGrant.GrantTypeEnum.GRANT_TYPES_OAUTH21.entrySet()) {
            String key = grantTypeMap.getKey();
            String value = grantTypeMap.getValue();
            AuthorizationGrant authorizationGrant = new AuthorizationGrant();
            authorizationGrantList.add(authorizationGrant);
            authorizationGrant.setClientId(clientId);
            authorizationGrant.setGrantType(key);
            authorizationGrant.setGrantTypeContent(value);
            genGrantUrlOauth21(key, oauthClientVO, authorizationGrant);
        }
        return authorizationGrantList;
    }

    private void genGrantUrl(
            String key, OauthClientVO oauthClientVO, AuthorizationGrant authorizationGrant) {
        if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE)) {
            authorizationGrant.setGrantTypeSet(getAuthorizationCode(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.IMPLICIT)) {
            authorizationGrant.setGrantTypeSet(getImplicit(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.CLIENT_CREDENTIALS)) {
            authorizationGrant.setGrantTypeSet(getClientCredentials(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.PASSWORD)) {
            authorizationGrant.setGrantTypeSet(getPassword(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE_ENHANCE)) {
            authorizationGrant.setGrantTypeSet(getAuthorizationCodeEnhance(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.EMAIL_CAPTCHA)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.PHONE_CAPTCHA)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.JWT_BEARER)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.DEVICE_CODE)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.TOKEN_EXCHANGE)) {

        }
    }

    private void genGrantUrlOauth21(
            String key, OauthClientVO oauthClientVO, AuthorizationGrant authorizationGrant) {
        if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE)) {
            authorizationGrant.setGrantTypeSet(getAuthorizationCodeOAuth21(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.IMPLICIT)) {
            authorizationGrant.setGrantTypeSet(getImplicit(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.CLIENT_CREDENTIALS)) {
            authorizationGrant.setGrantTypeSet(getClientCredentials(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.PASSWORD)) {
            authorizationGrant.setGrantTypeSet(getPassword(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE_ENHANCE)) {
            authorizationGrant.setGrantTypeSet(getAuthorizationCodeEnhance(oauthClientVO));
        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.EMAIL_CAPTCHA)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.PHONE_CAPTCHA)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.JWT_BEARER)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.DEVICE_CODE)) {

        } else if (Objects.equals(key, AuthorizationGrant.GrantTypeEnum.TOKEN_EXCHANGE)) {

        }
    }

    private TreeSet<AuthorizationGrant> getAuthorizationCodeEnhance(OauthClientVO oauthClientVO) {
        return null;
    }

    private TreeSet<AuthorizationGrant> getPassword(OauthClientVO oauthClientVO) {

        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String scopes = oauthClientVO.getScopes();
        String domain = oauthClientVO.getDomainName();
        String clientId = oauthClientVO.getClientId();
        TreeSet<AuthorizationGrant> grantSet =
                Sets.newTreeSet(Comparator.comparing(AuthorizationGrant::getStepNum));

        AuthorizationGrant passwordStep = new AuthorizationGrant();
        passwordStep.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumOne);
        grantSet.add(passwordStep);
        passwordStep.setClientId(clientId);
        passwordStep.setClientSecret(clientSecretBak);
        passwordStep.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth20Agreement));

        String authUrlPasswordUrl = genOauthTokenUrl(domain);
        passwordStep.setGrantTypeUrl(authUrlPasswordUrl);
        passwordStep.setMethod(AuthorizationGrant.GrantTypeEnum.POST);
        passwordStep.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);

        Map<String, String> params = new TreeMap<>();
        params.put(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrant.GrantTypeEnum.PASSWORD);
        params.put(OAuth2ParameterNames.USERNAME, "用户名");
        params.put(OAuth2ParameterNames.PASSWORD, "密码");
        params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
        params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecretBak);
        params.put(OAuth2ParameterNames.SCOPE, scopes);
        passwordStep.setParams(params);
        return grantSet;
    }

    private TreeSet<AuthorizationGrant> getClientCredentials(OauthClientVO oauthClientVO) {
        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String domain = oauthClientVO.getDomainName();
        String clientId = oauthClientVO.getClientId();
        TreeSet<AuthorizationGrant> grantSet =
                Sets.newTreeSet(Comparator.comparing(AuthorizationGrant::getStepNum));

        AuthorizationGrant clientCredentialsStep = new AuthorizationGrant();
        clientCredentialsStep.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumOne);
        grantSet.add(clientCredentialsStep);
        clientCredentialsStep.setClientId(clientId);
        clientCredentialsStep.setClientSecret(clientSecretBak);
        clientCredentialsStep.setGrantTypeAgreement(
                List.of(
                        AuthorizationGrant.GrantTypeEnum.Oauth20Agreement,
                        AuthorizationGrant.GrantTypeEnum.Oauth21Agreement));

        String authUrlClientCredentialsUrl = genClientCredentialsUrl(domain);
        clientCredentialsStep.setGrantTypeUrl(authUrlClientCredentialsUrl);
        clientCredentialsStep.setMethod(AuthorizationGrant.GrantTypeEnum.POST);
        clientCredentialsStep.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);

        Map<String, String> params = new TreeMap<>();
        params.put(
                OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrant.GrantTypeEnum.CLIENT_CREDENTIALS);
        params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
        params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecretBak);
        clientCredentialsStep.setParams(params);
        return grantSet;
    }

    private TreeSet<AuthorizationGrant> getImplicit(OauthClientVO oauthClientVO) {
        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String redirectUri = oauthClientVO.getRedirectUri();
        String scopes = oauthClientVO.getScopes();
        String domain = oauthClientVO.getDomainName();
        String clientId = oauthClientVO.getClientId();

        TreeSet<AuthorizationGrant> grantSet =
                Sets.newTreeSet(Comparator.comparing(AuthorizationGrant::getStepNum));
        AuthorizationGrant implicitStep = new AuthorizationGrant();
        implicitStep.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumOne);
        grantSet.add(implicitStep);

        implicitStep.setClientId(clientId);
        implicitStep.setClientSecret(clientSecretBak);
        implicitStep.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth20Agreement));
        String state = UUID.fastUUID().toString(true);
        String authUrlImplicitStep = genImplicitTokenUrl(domain, clientId, scopes, state, redirectUri);
        implicitStep.setGrantTypeUrl(authUrlImplicitStep);
        implicitStep.setMethod(AuthorizationGrant.GrantTypeEnum.GET);
        implicitStep.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);

        return grantSet;
    }

    private TreeSet<AuthorizationGrant> getAuthorizationCode(OauthClientVO oauthClientVO) {
        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String redirectUri = oauthClientVO.getRedirectUri();
        String scopes = oauthClientVO.getScopes();
        String domain = oauthClientVO.getDomainName();
        String clientId = oauthClientVO.getClientId();
        TreeSet<AuthorizationGrant> grantSet =
                Sets.newTreeSet(Comparator.comparing(AuthorizationGrant::getStepNum));

        AuthorizationGrant stepOne = new AuthorizationGrant();
        stepOne.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumOne);
        grantSet.add(stepOne);
        stepOne.setClientId(clientId);
        stepOne.setClientSecret(clientSecretBak);
        stepOne.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth20Agreement));
        String state = UUID.fastUUID().toString(true);
        String authUrlStepOne = genAuthorizationCodeUrl(domain, clientId, scopes, state, redirectUri);
        stepOne.setGrantTypeUrl(authUrlStepOne);
        stepOne.setMethod(AuthorizationGrant.GrantTypeEnum.GET);
        stepOne.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);

        AuthorizationGrant stepTwo = new AuthorizationGrant();
        stepTwo.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumTwo);
        grantSet.add(stepTwo);
        stepTwo.setClientId(clientId);
        stepTwo.setClientSecret(clientSecretBak);
        stepTwo.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth20Agreement));
        String authUrlStepTwo = genOauthTokenUrl(domain);
        stepTwo.setGrantTypeUrl(authUrlStepTwo);
        stepTwo.setMethod(AuthorizationGrant.GrantTypeEnum.POST);
        stepTwo.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);
        // 参数拼接: https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3
        Map<String, String> params = new TreeMap<>();
        params.put(
                OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE);
        params.put(OAuth2ParameterNames.CODE, "URL中的临时CODE");
        params.put(OAuth2ParameterNames.STATE, "URL中的state");
        params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
        params.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        stepTwo.setParams(params);
        return grantSet;
    }

    private TreeSet<AuthorizationGrant> getAuthorizationCodeOAuth21(OauthClientVO oauthClientVO) {
        String clientSecretBak = oauthClientVO.getClientSecretBak();
        String redirectUri = oauthClientVO.getRedirectUri();
        String scopes = oauthClientVO.getScopes();
        String domain = oauthClientVO.getDomainName();
        String clientId = oauthClientVO.getClientId();
        TreeSet<AuthorizationGrant> grantSet =
                Sets.newTreeSet(Comparator.comparing(AuthorizationGrant::getStepNum));

        AuthorizationGrant stepOne = new AuthorizationGrant();
        stepOne.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumOne);
        grantSet.add(stepOne);
        stepOne.setClientId(clientId);
        stepOne.setClientSecret(clientSecretBak);
        stepOne.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth21Agreement));
        String state = UUID.fastUUID().toString(true);
        String codeVerifier = generateCodeVerifier();
        String codeChallenge =
                generateCodeChallenge(codeVerifier, AuthorizationGrant.GrantTypeEnum.S256);
        String authUrlStepOne =
                genAuthorizationCodeOAuth21Url(
                        domain,
                        clientId,
                        codeChallenge,
                        AuthorizationGrant.GrantTypeEnum.S256,
                        scopes,
                        state,
                        redirectUri);
        stepOne.setGrantTypeUrl(authUrlStepOne);
        stepOne.setMethod(AuthorizationGrant.GrantTypeEnum.GET);
        stepOne.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);

        AuthorizationGrant stepTwo = new AuthorizationGrant();
        stepTwo.setStepNum(AuthorizationGrant.GrantTypeEnum.stepNumTwo);
        grantSet.add(stepTwo);
        stepTwo.setClientId(clientId);
        stepTwo.setClientSecret(clientSecretBak);
        stepTwo.setGrantTypeAgreement(List.of(AuthorizationGrant.GrantTypeEnum.Oauth21Agreement));
        String authUrlStepTwo = genOauthTokenUrl(domain);
        stepTwo.setGrantTypeUrl(authUrlStepTwo);
        stepTwo.setMethod(AuthorizationGrant.GrantTypeEnum.POST);
        stepTwo.setContentType(AuthorizationGrant.GrantTypeEnum.FORM_URLENCODED);
        // 参数拼接: https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.3
        Map<String, String> params = new TreeMap<>();
        params.put(
                OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrant.GrantTypeEnum.AUTHORIZATION_CODE);
        params.put(OAuth2ParameterNames.CODE, "URL中的临时CODE");
        params.put(OAuth2ParameterNames.STATE, "URL中的state");
        params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
        params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecretBak);
        params.put("code_verifier", codeVerifier);
        params.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        stepTwo.setParams(params);
        return grantSet;
    }

    public static String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        // 生成 32 个字节的随机值
        byte[] codeVerifierBytes = new byte[32];
        random.nextBytes(codeVerifierBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifierBytes);
    }

    public static String generateCodeChallenge(String codeVerifier, String method) {
        if ("S256".equals(method)) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
                return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            } catch (Exception e) {
                throw new RuntimeException("生成 codeChallenge 时出错", e);
            }
        } else if ("plain".equals(method)) {
            return codeVerifier;
        } else {
            throw new IllegalArgumentException("不支持的 codeChallengeMethod: " + method);
        }
    }

    public static void main(String[] args) {
        // 生成 codeVerifier
        String codeVerifier = generateCodeVerifier();
        System.out.println("codeVerifier: " + codeVerifier);

        // 生成 codeChallenge
        String codeChallenge = generateCodeChallenge(codeVerifier, "S256");
        System.out.println("codeChallenge: " + codeChallenge);
        // codeVerifier: e3fKD1-3KyrQ8f11MApC5mHeRQiWaQ_GHfl7QwvYbfI
        // codeChallenge: gEoIfA6Y_BZMTPO7kxXbsC2eWI1q5jSPB0Y4fNMtU-w
    }

    /**
     * 生成oauth2.1获取临时授权码模式
     *
     * @param domain 授权服务端域名
     * @param clientId 客户端ID
     * @param codeChallenge 授权域
     * @param codeChallengeMethod 授权域
     * @param scope 授权域
     * @param state 临时参数
     * @param redirectUri 重定向url
     * @return String
     */
    private String genAuthorizationCodeOAuth21Url(
            String domain,
            String clientId,
            String codeChallenge,
            String codeChallengeMethod,
            String scope,
            String state,
            String redirectUri) {
        return genHostOrigin(domain)
                + UriComponentsBuilder.fromPath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", codeChallengeMethod)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    /**
     * 授权码模式中-获取临时code的接口路径
     *
     * @param domain 授权服务器域名
     * @param clientId 客户端
     * @param scope 授权域
     * @param redirectUri 客户端的回调路径
     * @return String
     */
    static String genAuthorizationCodeUrl(
            String domain, String clientId, String scope, String state, String redirectUri) {
        return genHostOrigin(domain)
                + UriComponentsBuilder.fromPath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    /**
     * 隐式模式中-直接获取token的接口路径
     *
     * @param domain 授权服务器域名
     * @param clientId 客户端
     * @param scope 授权域
     * @param redirectUri 客户端的回调路径
     * @return String
     */
    static String genImplicitTokenUrl(
            String domain, String clientId, String scope, String state, String redirectUri) {
        return genHostOrigin(domain)
                + UriComponentsBuilder.fromPath("/oauth2/authorize")
                .queryParam("response_type", "token")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    /**
     * 客户端认证模式-直接获取token的接口
     *
     * @param domain 授权服务器域名
     * @return String
     */
    private String genClientCredentialsUrl(String domain) {
        return genHostOrigin(domain) + UriComponentsBuilder.fromPath("/oauth2/token").toUriString();
    }

    /**
     * 获取accessToken的接口路径
     *
     * @param domain 授权服务器域名
     * @return String
     */
    static String genOauthTokenUrl(String domain) {
        return genHostOrigin(domain) + UriComponentsBuilder.fromPath("/oauth2/token").toUriString();
    }

    /**
     * 授权服务器域名前缀
     *
     * @param domain 授权服务器域名前缀,例如:
     *     <p><a
     *     href="http://dev.authorization.life/dev-api/auth-life">http://dev.authorization.life/dev-api/auth-life</a>
     *     <p>
     * @return String
     */
    static String genHostOrigin(String domain) {
        String hostOriginFormat = "http://%s/dev-api/auth-life";
        return String.format(hostOriginFormat, domain);
    }
}
