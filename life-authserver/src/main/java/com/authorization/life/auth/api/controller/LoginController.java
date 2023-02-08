package com.authorization.life.auth.api.controller;

import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.security.util.Captcha;
import com.authorization.life.auth.infra.security.util.RedisCaptchaValidator;
import com.authorization.redis.start.service.StringRedisService;
import com.authorization.utils.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private OauthClientService oauthClientService;

    @Operation(summary = "通过请求的域名获取client信息.")
    @GetMapping("/client-domain/{domainName}/grant-type/{grantType}")
    public R<OauthClientVO> clientByDomain(@Parameter(description = "请求路径中的域名", example = "www.authorization.life", required = true)
                                           @PathVariable String domainName,
                                           @Parameter(description = "请求路径中的授权类型", example = "authorization_code", required = true)
                                           @PathVariable String grantType) {
        return R.ok(oauthClientService.clientByDomain(domainName, grantType));
    }

    @Operation(summary = "发送手机验证码")
    @GetMapping("/send-sms-code")
    public R<String> sendSmsCode(@RequestParam(name = "phone") String phone) {
        Captcha captcha = RedisCaptchaValidator.create(stringRedisService);
        String code = captcha.getCode();
        return R.ok(code);
    }

    @Operation(summary = "发送邮箱验证码")
    @GetMapping("/send-email-code")
    public R<String> sendEmailCode(@RequestParam(name = "email") String email) {
        Captcha captcha = RedisCaptchaValidator.create(stringRedisService);
        String code = captcha.getCode();
        return R.ok(code);
    }

    @Operation(summary = "图片验证码")
    @GetMapping("/picture-code")
    public R<String> pictureCode() {
        Captcha captcha = RedisCaptchaValidator.create(stringRedisService);
        String code = captcha.getCode();
        return R.ok(code);
    }

    @Operation(summary = "用户重置密码")
    @PostMapping("/reset-password")
    public R<Void> resetPassword(@RequestBody LifeUserDTO lifeUser) {
        return R.ok();
    }

}
