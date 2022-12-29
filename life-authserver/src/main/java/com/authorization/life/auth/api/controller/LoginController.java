package com.authorization.life.auth.api.controller;

import com.authorization.life.auth.infra.security.util.Captcha;
import com.authorization.life.auth.infra.security.util.RedisCaptchaValidator;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户登录,发送验证码,注册
 *
 * @author wangjunming
 * @date 2022/12/29 16:33
 */
@Tag(name = "登录管理控制层", description = "登录管理控制层")
@RestController
@RequestMapping("/v1/user-login")
public class LoginController {

    @Resource
    private StringRedisService stringRedisService;

    @Operation(summary = "发送验证码")
    @GetMapping("/send-sms-code")
    public R<String> sendSmsCode(@RequestParam(name = "phone") String phone) {
        Captcha captcha = RedisCaptchaValidator.create(stringRedisService);
        String code = captcha.getCode();
        return R.ok(code);
    }

}
