package com.authorization.life.auth.api.controller;

import cn.hutool.core.lang.Assert;
import com.authorization.core.exception.handle.CommonException;
import com.authorization.core.security.entity.UserHelper;
import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.auth.infra.security.util.Captcha;
import com.authorization.life.auth.infra.security.util.RedisCaptchaValidator;
import com.authorization.life.mail.start.MailConstant;
import com.authorization.life.mail.start.MailSendService;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 登录控制层
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Slf4j
@Tag(name = "登录控制层", description = "获取当前登录用户信息,权限信息")
@RestController
@RequestMapping("/oauth")
public class OauthController {


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OauthClientService oauthClientService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailSendService mailSendService;


    @Operation(summary = "通过请求路径中的域名获取client信息")
    @GetMapping("/clientByDomain")
    public Result<OauthClientVO> clientByDomain(@Parameter(description = "请求路径中的域名", example = "www.authorization.life", required = true)
                                                @RequestParam(name = "domainName") String domainName) {
        return Result.ok(oauthClientService.clientByDomain(domainName));
    }

    @Operation(summary = "用户邮箱注册")
    @PostMapping("/email-register")
    public Result<String> emailRegister(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok(userService.emailRegister(lifeUser));
    }

    @Operation(summary = "发送手机验证码")
    @GetMapping("/send-sms-code")
    public Result<String> sendSmsCode(@RequestParam(name = "phone") String phone) {
        Captcha captcha = RedisCaptchaValidator.create(redisUtil);
        String code = captcha.getCode();
        return Result.ok(code);
    }

    @Operation(summary = "发送邮箱验证码")
    @PostMapping("/send-email-code")
    public Result<String> sendEmailCode(@RequestBody LifeUserDTO lifeUser) {
        //验证邮箱是否重复
        Boolean emailExist = userService.validateEmailExist(lifeUser);
        Assert.isFalse(emailExist, "邮箱已注册.");
        //生成验证码
        Captcha captcha = RedisCaptchaValidator.createNumCaptcha(redisUtil);
        //发送邮件
        try {
            mailSendService.sendEmail(MailConstant.REGISTER_CAPTCHA_CODE_TEMPLATE, lifeUser.getEmail(), Map.of("captchaCode", captcha.getCode()));
        } catch (Exception e) {
            log.error("发送邮件异常", e);
            RedisCaptchaValidator.verify(redisUtil, captcha.getUuid(), captcha.getCode());
            throw new CommonException("邮件发送失败,请输入正确的邮箱.");
        }
        return Result.ok(captcha.getUuid());
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/self-user")
    public Result<UserDetail> getCurrentUser() {
        return Result.ok(UserHelper.getUserDetail());
    }

    @Operation(summary = "图片验证码")
    @GetMapping("/picture-code")
    public Result<Captcha> pictureCode() {
        Captcha captcha = RedisCaptchaValidator.createNumCaptcha(redisUtil);
        String code = captcha.getCode();
        return Result.ok(captcha);
    }

    @Operation(summary = "用户重置密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok();
    }

    @Autowired
    private JwtDecoder jwtDecoder;

    @Operation(summary = "解析jwt信息")
    @PostMapping("/decode-jwt")
    public Result<Map<String, Object>> decodeJwt(@RequestParam("jwtToken") String jwtToken) {
        Jwt decode = jwtDecoder.decode(jwtToken);
        return Result.ok(decode.getClaims());
    }

}
