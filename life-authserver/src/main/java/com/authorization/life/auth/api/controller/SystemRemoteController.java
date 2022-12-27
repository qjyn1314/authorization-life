package com.authorization.life.auth.api.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.common.log.LogAdvice;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.utils.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 测试system-dubbo接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:42
 */
@Tag(name = "测试system-dubbo接口", description = "测试system-dubbo接口")
@RestController
@RequestMapping("/v1/system-remote")
public class SystemRemoteController {

    @Resource
    private SystemRemoteService systemRemoteService;

    @Operation(summary = "获取-SystemRemote-当前时间", description = "获取-SystemRemote-当前时间")
    @LogAdvice
    @GetMapping("/remote-date")
    public R<String> getSystemRemoteNowDate() {
        return systemRemoteService.getSystemRemoteNowDate();
    }


}