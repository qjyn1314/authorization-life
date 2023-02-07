package com.authorization.life.auth.api.controller;

import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.authorization.utils.result.R;
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

    @Operation(summary = "获取当前登录用户信息")
    @PostMapping("/page")
    public R<PageInfo<LifeUser>> page(@RequestBody LifeUser lifeUser) {
        PageInfo<LifeUser> userPageInfo = userService.page(lifeUser);
        return R.ok(userPageInfo);
    }

}
