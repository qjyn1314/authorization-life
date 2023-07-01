package com.authorization.life.auth.test;

import com.authorization.utils.json.JsonDateUtil;
import com.authorization.utils.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author wangjunming
 */
@Tag(name = "认证服务测试控制层", description = "认证服务测试控制层")
@RestController
@RequestMapping("/v1/test")
public class OauthServerTestCtrl {

    @Operation(summary = "获取当前时间")
    @PostMapping("/now-date")
    public Result<String> nowDate() {
        return Result.ok(JsonDateUtil.formatLocalDateTime(LocalDateTime.now()));
    }

}
