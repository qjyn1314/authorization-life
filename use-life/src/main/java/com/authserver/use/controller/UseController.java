package com.authserver.use.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authserver.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UseController {

    @GetMapping("/now-date")
    public Result<String> getCurrentUser() {
        return Result.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

}
