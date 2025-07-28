package com.authorization.life.auth.app.service;

import com.authorization.life.auth.app.dto.OauthClientDTO;
import com.authorization.life.auth.app.vo.AuthorizationGrant;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * oauth客户端表
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
public interface OauthClientService {


    OauthClientVO selectClientByClientId(String id);

    OauthClientVO clientByDomain(String domainName);

    PageInfo<OauthClientVO> page(OauthClientDTO clientDTO);

    List<OauthClientVO> genAuthorizationUrl(String clientId, String hostOrigin);

    OauthClientVO saveClient(OauthClientDTO clientDTO);

    OauthClientVO getClient(String clientId);

    Boolean delClient(String clientId);

    List<AuthorizationGrant> genGrantTypeUrl(String clientId);

}
