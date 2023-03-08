package com.authorization.life.system.api.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.common.log.LogAdvice;
import com.authorization.life.system.infra.constant.ErrorMsgConstant;
import com.authorization.utils.message.MsgResource;
import com.authorization.utils.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wangjunming
 * @since 2022/6/4 2:07
 */
@RestController
@RequestMapping("/use")
@Tag(name = "测试服务控制层", description = "用来给开发人员测试使用的")
public class UseController {

    @Autowired
    private Validator validator;

    @Operation(summary = "获取当前时间", description = "直接获取当前时间格式化后的字符串")
    @LogAdvice
    @GetMapping("/now-date")
    public Result<String> getCurrentUser() {
        return Result.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

    @Operation(summary = "获取国际化消息提示-通过抛出异常获得提示信息", description = "获取国际化消息提示-通过抛出异常获得提示信息")
    @LogAdvice
    @GetMapping("/in18/exception")
    public Result<Void> getIn18ByException() {

        Integer i = 10 / 0;

        return Result.ok();
    }

    @Operation(summary = "获取国际化消息提示-通过直接return获取提示消息", description = "获取国际化消息提示-通过直接return获取提示消息")
    @LogAdvice
    @GetMapping("/in18/return")
    public Result<Date> getIn18ByReturn() {
        String nowDateFormat = DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN);
        return Result.fail(ErrorMsgConstant.DATA_NOT_EXIST.getMsgCode());
    }

}
