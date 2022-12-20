package com.authorization.life.auth.app.service.impl;

import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.entity.OauthClient;
import com.authorization.life.auth.infra.mapper.OauthClientMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
    public OauthClient selectClientByClientId(String id) {
        return mapper.selectOne(Wrappers.lambdaQuery(OauthClient.class).eq(OauthClient::getClientId,id));
    }
}
