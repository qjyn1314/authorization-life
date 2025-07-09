package com.authorization.life.auth.api.controller;

import com.authorization.common.exception.enums.SecurityErrorEnums;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.UserDetail;
import com.authorization.utils.security.UserDetailUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangjunming
 * @since 2025-07-09 14:13
 */
@Slf4j
@Tag(name = "oauth测试控制层", description = "oauth测试控制层")
@RestController
@RequestMapping("/oauth-test")
public class OauthTestController {

  @Autowired private JwtDecoder jwtDecoder;

  @Operation(summary = "解析请求头中的jwt信息")
  @PostMapping("/decode-jwt")
  public Result<Map<String, Object>> decodeJwt(@RequestParam("jwtToken") String jwtToken) {
    Jwt decode = jwtDecoder.decode(jwtToken);
    return Result.ok(decode.getClaims());
  }

  @Operation(summary = "测试国际化异常信息")
  @GetMapping("/in8-s")
  public Result<String> in8s() {

    UserDetailUtil.setUserContext(UserDetail.anonymous());
    String formattedMsg = SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg();
    log.info("SecurityErrorEnums.LOGIN_IS_INVALID.formatMsg()--{}", formattedMsg);
    UserDetailUtil.remove();

    return Result.ok(formattedMsg);
  }
}
