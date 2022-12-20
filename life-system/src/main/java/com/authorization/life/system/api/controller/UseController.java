package com.authorization.life.system.api.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.common.log.LogAdvice;
import com.authorization.utils.result.R;
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

    @LogAdvice
    @GetMapping("/now-date")
    public R<String> getCurrentUser() {
        return R.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

}
