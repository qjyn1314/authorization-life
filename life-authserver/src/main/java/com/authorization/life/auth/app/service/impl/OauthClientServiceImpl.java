package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.authorization.common.util.RequestUtils;
import com.authorization.core.exception.handle.CommonException;
import com.authorization.core.proxy.CurrentProxy;
import com.authorization.life.auth.app.dto.OauthClientDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.OauthClient;
import com.authorization.life.auth.infra.mapper.OauthClientMapper;
import com.authorization.utils.converter.BeanConverter;
import com.authorization.utils.security.SecurityCoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

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
public class OauthClientServiceImpl implements OauthClientService, CurrentProxy<OauthClientService> {

    @Autowired
    private OauthClientMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OauthClientVO selectClientByClientId(String id) {
        Assert.notBlank(id, () -> new CommonException("clientId不能为空."));
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId, id));
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
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, domainName));
        if (Objects.isNull(oauthClient)) {
            oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, SecurityCoreService.DEFAULT_DOMAIN));
        }
        Assert.notNull(oauthClient, "根据域名未找到client信息.");
        return BeanConverter.convert(oauthClient, OauthClientVO.class, Map.of(OauthClient.FIELD_CLIENT_SECRET_BAK, OauthClient.FIELD_CLIENT_SECRET));
    }

    @Override
    public PageInfo<OauthClientVO> page(OauthClientDTO clientDTO) {
        Page<OauthClientVO> startPage = PageHelper.startPage(clientDTO.getPageNum(), clientDTO.getPageSize());
        return startPage.doSelectPageInfo(() -> clientList(clientDTO));
    }

    private List<OauthClientVO> clientList(OauthClientDTO clientDTO) {
        List<OauthClient> oauthClients = mapper.page(clientDTO);
        return oauthClients.stream().map(oauthClient ->
                Convert.convert(OauthClientVO.class, oauthClient)).collect(Collectors.toList());
    }


    /**
     * 授权码模式中-生成获取临时code的URL
     *
     * @param hostOrigin  授权服务器域名前缀
     * @param code        授权码模式
     * @param clientId    客户端
     * @param scope       授权域
     * @param redirectUri 客户端的回调路径
     * @return String
     */
    public static String genAuthorizationCodeUrl(String hostOrigin, String clientId, String scope, String redirectUri) {
        return hostOrigin + UriComponentsBuilder
                .fromPath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("state", UUID.fastUUID().toString(true))
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    /**
     * 生成获取accessToken的URL
     *
     * @param hostOrigin 授权服务器域名前缀
     * @param grantType  授权模式
     * @return String
     */
    public static String genAuthorizationTokenUrl(String hostOrigin, AuthorizationGrantType grantType) {
        String url = "";
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(grantType)) {
            url = hostOrigin + UriComponentsBuilder
                    .fromPath("/oauth2/token")
                    .toUriString();
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            url = hostOrigin + UriComponentsBuilder
                    .fromPath("/oauth2/authorize")
                    .toUriString();
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
    public static Map<String, String> paramsMap(AuthorizationGrantType grantType, String redirectUri, String clientId, String clientSecret) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(grantType)) {
            Map<String, String> params = new HashMap<>(6);
            params.put(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
            params.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
            params.put(OAuth2ParameterNames.CLIENT_ID, clientId);
            params.put(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
            params.put(OAuth2ParameterNames.CODE, "URL中的临时CODE");
            params.put(OAuth2ParameterNames.STATE, "URL中的state");
            return params;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            Map<String, String> params = new HashMap<>(3);
            params.put(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
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

        Set<String> grantTypeSet = Arrays.stream(grantTypes.split(StrUtil.COMMA)).sorted().collect(Collectors.toCollection(TreeSet::new));
        Set<String> scopeSet = Arrays.stream(scopes.split(StrUtil.COMMA)).collect(Collectors.toSet());
        Set<String> urlList = Arrays.stream(redirectUri.split(StrUtil.COMMA)).collect(Collectors.toSet());

        log.info("hostOrigin->{}", hostOrigin);
        Set<String> authUrlSet = new HashSet<>();
        List<OauthClientVO> clientUrls = CollUtil.newArrayList();

        for (String url : urlList) {
            for (String code : grantTypeSet) {
                for (String scope : scopeSet) {
                    if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(code)) {
                        String authUrl = genAuthorizationCodeUrl(hostOrigin, clientId, scope, url);
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
                        oauthClient02.setRedirectUrl(genAuthorizationTokenUrl(hostOrigin, AuthorizationGrantType.AUTHORIZATION_CODE));
                        oauthClient02.setRedirectUri(url);
                        oauthClient02.setParams(paramsMap(AuthorizationGrantType.AUTHORIZATION_CODE, url, clientId, clientSecretBak));
                        clientUrls.add(oauthClient02);

                    } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(code)) {
                        OauthClientVO oauthClient02 = new OauthClientVO();
                        BeanUtils.copyProperties(oauthClientVO, oauthClient02);
                        oauthClient02.setGrantTypes(code);
                        oauthClient02.setMethod(POST);
                        oauthClient02.setRedirectUrl(genAuthorizationTokenUrl(hostOrigin, AuthorizationGrantType.CLIENT_CREDENTIALS));
                        oauthClient02.setRedirectUri(url);
                        oauthClient02.setParams(paramsMap(AuthorizationGrantType.CLIENT_CREDENTIALS, url, clientId, clientSecretBak));
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
        Assert.isFalse("passport".equals(clientDTO.getClientId()), () -> new CommonException("\"passport\"为基本client, 不允许修改"));
        OauthClient oauthClient = Convert.convert(OauthClient.class, clientDTO);

        OauthClient existOauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId, clientDTO.getClientId()));

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
        Assert.isFalse("passport".equals(clientId), () -> new CommonException("\"passport\"为基本client, 不允许删除"));
        OauthClientVO oauthClientVO = selectClientByClientId(clientId);
        LambdaQueryWrapper<OauthClient> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OauthClient::getClientId, clientId);
        int delete = mapper.delete(wrapper);
        return delete > 0;
    }

}
