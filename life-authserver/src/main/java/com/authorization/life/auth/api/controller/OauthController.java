package com.authorization.life.auth.api.controller;

import cn.hutool.core.lang.Assert;
import com.authorization.common.exception.handle.CommonException;
import com.authorization.life.security.start.entity.UserHelper;
import com.authorization.life.auth.app.constant.Inspirational;
import com.authorization.life.auth.app.constant.RedisKeyValid;
import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.life.mail.start.MailConstant;
import com.authorization.life.mail.start.MailSendService;
import com.authorization.redis.start.model.Captcha;
import com.authorization.redis.start.model.RedisCaptchaValid;
import com.authorization.redis.start.util.RedisUtil;
import com.authorization.utils.result.Result;
import com.authorization.utils.security.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "获取随机的励志句子")
    @GetMapping("/sentence")
    public Result<String> getSentence() {
        return Result.ok(Inspirational.getSentence());
    }

    @Operation(summary = "通过请求路径中的域名获取client信息")
    @PostMapping("/client")
    public Result<OauthClientVO> clientByDomain(@RequestBody Map<String, String> param) {
        return Result.ok(oauthClientService.clientByDomain(param.getOrDefault("domain", "")));
    }

    @Operation(summary = "获取图片验证码")
    @GetMapping("/picture-code")
    public Result<Captcha> pictureCode(@RequestParam(required = false, value = "uuid") String uuid) {
        boolean repeatPictureCode = RedisCaptchaValid.verifyRepeatPictureCode(redisUtil, uuid);
        Assert.isFalse(repeatPictureCode, "验证码未验证, 请勿重新获取.");
        return Result.ok(RedisCaptchaValid.create(redisUtil));
    }

    @Operation(summary = "发送邮箱注册验证码")
    @PostMapping("/send-email-code")
    public Result<String> sendEmailCode(@RequestBody LifeUserDTO lifeUser) {
        //验证邮箱是否重复
        Boolean emailExist = userService.validateEmailExist(lifeUser);
        Assert.isFalse(emailExist, "邮箱已注册.");
        //邮箱验证码是否在十分钟内重复发送.
        RedisKeyValid.validEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
        //生成验证码
        Captcha captcha = RedisCaptchaValid.createNumCaptcha(redisUtil);
        //发送邮件
        try {
            mailSendService.sendEmail(MailConstant.REGISTER_CAPTCHA_CODE_TEMPLATE, lifeUser.getEmail(), Map.of("captchaCode", captcha.getCode()));
        } catch (Exception e) {
            log.error("发送邮件异常", e);
            RedisCaptchaValid.verify(redisUtil, captcha.getUuid(), captcha.getCode());
            RedisKeyValid.delEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
            throw new CommonException("邮件发送失败,请输入正确的邮箱.");
        }
        return Result.ok(captcha.getUuid());
    }

    @Operation(summary = "用户邮箱注册")
    @PostMapping("/email-register")
    public Result<String> emailRegister(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok(userService.emailRegister(lifeUser));
    }

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/self-user")
    public Result<UserDetail> getCurrentUser() {
        return Result.ok(UserHelper.getUserDetail());
    }


    @Operation(summary = "发送邮箱重置密码验证码")
    @PostMapping("/send-email-code-reset-pwd")
    public Result<String> sendEmailCodeResetPwd(@RequestBody LifeUserDTO lifeUser) {
        //验证邮箱是否重复
        Boolean emailExist = userService.validateEmailExist(lifeUser);
        Assert.isTrue(emailExist, "邮箱未注册.");
        //邮箱验证码是否在十分钟内重复发送.
        RedisKeyValid.validEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
        //生成验证码
        Captcha captcha = RedisCaptchaValid.createNumCaptcha(redisUtil);
        //发送邮件
        try {
            mailSendService.sendEmail(MailConstant.RESET_PASSWORD_CAPTCHA_CODE_TEMPLATE, lifeUser.getEmail(), Map.of("captchaCode", captcha.getCode()));
        } catch (Exception e) {
            log.error("发送邮件异常", e);
            RedisCaptchaValid.verify(redisUtil, captcha.getUuid(), captcha.getCode());
            RedisKeyValid.delEmailRepeatSendCode(redisUtil, lifeUser.getEmail());
            throw new CommonException("邮件发送失败,请输入正确的邮箱.");
        }
        return Result.ok(captcha.getUuid());
    }


    @Operation(summary = "用户重置密码")
    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@RequestBody LifeUserDTO lifeUser) {
        userService.resetPassword(lifeUser);
        return Result.ok();
    }

    @Operation(summary = "发送手机登录验证码")
    @GetMapping("/send-sms-code-login")
    public Result<String> sendLoginSmsCode(@RequestParam(name = "phone") String phone) {
        Captcha captcha = RedisCaptchaValid.create(redisUtil);
        String code = captcha.getCode();
        return Result.ok(code);
    }

    @Operation(summary = "发送邮箱登录验证码")
    @GetMapping("/send-email-code-login")
    public Result<String> sendLoginEmailCode(@RequestParam(name = "email") String email) {
        // 验证邮箱是否重复
        Boolean emailExist = userService.validateEmailExist(new LifeUserDTO().setEmail(email));
        Assert.isTrue(emailExist, "邮箱未注册.");
        //邮箱验证码是否在十分钟内重复发送.
        RedisKeyValid.validEmailRepeatSendCode(redisUtil, email);
        //生成验证码
        Captcha captcha = RedisCaptchaValid.createNumCaptcha(redisUtil);
        //发送邮件
        try {
            mailSendService.sendEmail(MailConstant.LOGIN_CAPTCHA_CODE_TEMPLATE, email, Map.of("captchaCode", captcha.getCode()));
        } catch (Exception e) {
            log.error("发送邮件异常", e);
            RedisCaptchaValid.verify(redisUtil, captcha.getUuid(), captcha.getCode());
            RedisKeyValid.delEmailRepeatSendCode(redisUtil, email);
            throw new CommonException("邮件发送失败,请输入正确的邮箱.");
        }
        return Result.ok(captcha.getUuid());
    }

}
