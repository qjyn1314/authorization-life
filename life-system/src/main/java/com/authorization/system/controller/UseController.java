package com.authorization.system.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.start.util.result.Res;
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
    public Res<String> getCurrentUser() {
        return Res.ok(DateUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN));
    }

}
