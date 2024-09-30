package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.lang.Assert;
import com.authorization.core.exception.handle.CommonException;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.OauthClient;
import com.authorization.life.auth.infra.mapper.OauthClientMapper;
import com.authorization.utils.converter.BeanConverter;
import com.authorization.utils.security.SecurityCoreService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * oauth客户端表
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Slf4j
@Service
public class OauthClientServiceImpl implements OauthClientService {

    @Autowired
    private OauthClientMapper mapper;

    @Override
    public OauthClientVO selectClientByClientId(String id) {
        Assert.notBlank(id, () -> new CommonException("clientId不能为空."));
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId, id));
        Assert.notNull(oauthClient, () -> new CommonException("根据clientId未找到Client信息."));
        return BeanConverter.convert(oauthClient, OauthClientVO.class);
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
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, domainName));
        if (Objects.isNull(oauthClient)) {
            oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, SecurityCoreService.DEFAULT_DOMAIN));
        }
        Assert.notNull(oauthClient, "根据域名未找到client信息.");
        return BeanConverter.convert(oauthClient, OauthClientVO.class, Map.of(OauthClient.FIELD_CLIENT_SECRET_BAK, OauthClient.FIELD_CLIENT_SECRET));
    }
}
