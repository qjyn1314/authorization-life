package com.authorization.life.auth.api.controller;

import com.authorization.core.entity.UserDetail;
import com.authorization.core.entity.UserHelper;
import com.authorization.life.auth.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录控制层
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Tag(name = "登录控制层", description = "获取当前登录用户信息,权限信息")
@RestController
@RequestMapping("/v1/oauth")
public class OauthController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/self-user")
    public UserDetail getCurrentUser() {
        return UserHelper.getUserDetail();
    }

}