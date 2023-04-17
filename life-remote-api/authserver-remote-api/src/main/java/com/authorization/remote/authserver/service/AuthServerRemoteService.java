package com.authorization.remote.authserver.service;

import com.authorization.remote.authserver.AuthServerApiRes;
import com.authorization.remote.authserver.vo.UserDetailVO;

/**
 * auth-life 服务 暴露的远程接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:17
 */
public interface AuthServerRemoteService {


    AuthServerApiRes<UserDetailVO> getUserByToken(String accessToken);


}
