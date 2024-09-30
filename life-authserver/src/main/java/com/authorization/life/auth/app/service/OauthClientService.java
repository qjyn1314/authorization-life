package com.authorization.life.auth.app.service;

import com.authorization.life.auth.app.vo.OauthClientVO;

/**
 * oauth客户端表
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
public interface OauthClientService {


    OauthClientVO selectClientByClientId(String id);

    OauthClientVO clientByDomain(String domainName);

}
