package com.authserver.life.service.impl;

import com.authserver.life.entity.OauthClient;
import com.authserver.life.mapper.OauthClientMapper;
import com.authserver.life.service.OauthClientService;
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
