package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        clientDTO = Objects.isNull(clientDTO) ? new OauthClientDTO() : clientDTO;
        OauthClient clientQuery = Convert.convert(OauthClient.class, clientDTO);
        List<OauthClient> oauthClients = mapper.page(clientQuery);
        return oauthClients.stream().map(oauthClient ->
                Convert.convert(OauthClientVO.class, oauthClient)).collect(Collectors.toList());
    }


    @Override
    public List<OauthClientVO> genAuthorizationUrl(String clientId) {
        OauthClientVO oauthClientVO = selectClientByClientId(clientId);
        String redirectUri = oauthClientVO.getRedirectUri();
        if (StrUtil.isBlank(redirectUri)) {
            return List.of();
        }
        Set<String> urlList = Arrays.stream(redirectUri.split(StrUtil.COMMA)).collect(Collectors.toSet());

        // 获取到此次请求的域名和请求方式
        String contextPath = RequestUtils.getRequest().getContextPath();
        String servletPath = RequestUtils.getRequest().getServletPath();
        String remoteAddr = RequestUtils.getRequest().getRemoteAddr();
        String remoteHost = RequestUtils.getRequest().getRemoteHost();
        int remotePort = RequestUtils.getRequest().getRemotePort();
        String localAddr = RequestUtils.getRequest().getLocalAddr();
        String localName = RequestUtils.getRequest().getLocalName();
        int localPort = RequestUtils.getRequest().getLocalPort();
        String serverName = RequestUtils.getRequest().getServerName();
        int serverPort = RequestUtils.getRequest().getServerPort();
        String scheme = RequestUtils.getRequest().getScheme();


        List<OauthClientVO> clientUrls = CollUtil.newArrayList();
        for (String url : urlList) {
            OauthClientVO oauthClient = Convert.convert(OauthClientVO.class, oauthClientVO);


            oauthClient.setRedirectUri(contextPath);
            clientUrls.add(oauthClient);
        }

        return clientUrls;
    }

}
