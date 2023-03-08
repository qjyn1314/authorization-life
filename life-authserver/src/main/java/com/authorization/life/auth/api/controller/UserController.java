package com.authorization.life.auth.api.controller;

import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.LifeUserVO;
import com.authorization.utils.result.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户管理控制层
 *
 * @author wangjunming
 * @date 2022/12/29 16:21
 */
@Tag(name = "用户管理控制层", description = "用户管理控制层")
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok();
    }

    @Operation(summary = "用户列表")
    @PostMapping("/page")
    public Result<PageInfo<LifeUserVO>> page(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok(userService.page(lifeUser));
    }

    @Operation(summary = "注册用户审核通过")
    @PostMapping("/user-approved")
    public Result<Void> userApproved(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok();
    }

    @Operation(summary = "注册用户审核拒绝")
    @PostMapping("/user-audit-reject")
    public Result<Void> userAuditReject(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok();
    }

    @Operation(summary = "用户解锁")
    @PostMapping("/user-unlock")
    public Result<Void> userUnlock(@RequestBody LifeUserDTO lifeUser) {
        return Result.ok();
    }

}
