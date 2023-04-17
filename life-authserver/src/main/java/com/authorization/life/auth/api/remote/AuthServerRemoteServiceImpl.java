package com.authorization.life.auth.api.remote;

import com.authorization.remote.authserver.AuthServerApiRes;
import com.authorization.remote.authserver.service.AuthServerRemoteService;
import com.authorization.remote.authserver.vo.UserDetailVO;
import org.springframework.stereotype.Service;

/**
 * auth-life 服务 暴露的远程接口 的具体实现
 *
 * @author wangjunming
 * @date 2023/1/9 13:21
 */
@Service
public class AuthServerRemoteServiceImpl implements AuthServerRemoteService {

    /**
     * 根据jwtToken获取当前登录用户信息
     *
     * @param accessToken 前端传入的accessToken,
     */
    @Override
    public AuthServerApiRes<UserDetailVO> getUserByToken(String accessToken) {

        return null;
    }
}
