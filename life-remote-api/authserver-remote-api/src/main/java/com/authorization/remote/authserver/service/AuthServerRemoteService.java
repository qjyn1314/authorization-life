package com.authorization.remote.authserver.service;

import com.authorization.remote.authserver.AuthRemoteRes;
import com.authorization.remote.authserver.vo.UserDetailVO;

/**
 * auth-life 服务 暴露的远程接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:17
 */
public interface AuthServerRemoteService {

    /**
     * 通过解析Oauth2Server生成的accesstoken获取到用户信息
     *
     * @param accessToken
     * @return UserDetailVO
     */
    AuthRemoteRes<UserDetailVO> getUserByAccessToken(String accessToken);

    /**
     * 通过解析网关工程创建jwtToken获取到用户信息
     *
     * @param jwtToken
     * @return UserDetailVO
     */
    AuthRemoteRes<UserDetailVO> getUserByJwtToken(String jwtToken);


}
