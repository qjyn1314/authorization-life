package com.authorization.life.auth.api.remote;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.remote.authserver.AuthRemoteRes;
import com.authorization.remote.authserver.service.AuthServerRemoteService;
import com.authorization.remote.authserver.vo.UserDetailVO;
import com.authorization.utils.security.JwtService;
import com.authorization.utils.security.SecurityCoreService;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

/**
 * auth-life 服务 暴露的远程接口 的具体实现
 *
 * @author wangjunming
 * @date 2023/1/9 13:21
 */
@Slf4j
@Service
public class AuthServerRemoteServiceImpl implements AuthServerRemoteService {

  @Autowired private JwtService jwtService;
  @Autowired private JwtDecoder jwtDecoder;
  @Autowired private RedisUtil redisUtil;

  @Override
  public AuthRemoteRes<UserDetailVO> getUserByAccessToken(String accessToken) {
    if (StrUtil.isBlank(accessToken)) {
      return AuthRemoteRes.fail();
    }
    String token =
        Optional.ofNullable(jwtDecoder.decode(accessToken).getClaims())
            .orElse(Map.of())
            .getOrDefault(SecurityCoreService.CLAIM_TOKEN_KEY, "")
            .toString();
    if (StrUtil.isBlank(token)) {
      return AuthRemoteRes.fail();
    }
    String userInfoJson = redisUtil.get(SecurityCoreService.getUserTokenKey(token));
    try {
      return AuthRemoteRes.ok(JSON.parseObject(userInfoJson, UserDetailVO.class));
    } catch (Exception e) {
      log.error("将json转换为用户信息失败->", e);
      return AuthRemoteRes.fail();
    }
  }

  @Override
  public AuthRemoteRes<UserDetailVO> getUserByJwtToken(String jwtToken) {
    if (StrUtil.isBlank(jwtToken)) {
      return AuthRemoteRes.fail();
    }
    Map<String, Object> claimsFromJwtToken = jwtService.getClaimsFromJwtToken(jwtToken);
    if (CollUtil.isEmpty(claimsFromJwtToken)) {
      return AuthRemoteRes.fail();
    }
    try {
      return AuthRemoteRes.ok(
          JSON.parseObject(JSON.toJSONString(claimsFromJwtToken), UserDetailVO.class));
    } catch (Exception e) {
      return AuthRemoteRes.fail();
    }
  }
}
