package com.authorization.life.system.api.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.common.log.LogAdvice;
import com.authorization.utils.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;
import java.time.LocalDateTime;

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
    public R<String> getCurrentUser() {
        return R.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

}
