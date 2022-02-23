package com.authserver.life.service.impl;

import com.authserver.life.mapper.OauthClientMapper;
import com.authserver.life.service.OauthClientService;
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



}
