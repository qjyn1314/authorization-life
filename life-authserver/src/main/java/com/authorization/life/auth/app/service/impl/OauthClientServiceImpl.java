package com.authorization.life.auth.app.service.impl;

import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.entity.OauthClient;
import com.authorization.life.auth.infra.mapper.OauthClientMapper;
import com.authorization.utils.converter.BeanConverter;
import com.authorization.utils.security.SecurityConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * oauth客户端表
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Service
public class OauthClientServiceImpl implements OauthClientService {

    @Autowired
    private OauthClientMapper mapper;

    @Override
    public OauthClientVO selectClientByClientId(String id) {
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId, id));
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
        OauthClient oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, domainName));
        if (Objects.isNull(oauthClient)) {
            oauthClient = mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getDomainName, SecurityConstant.DEFAULT_DOMAIN));
        }
        return BeanConverter.convert(oauthClient, OauthClientVO.class);
    }
}
