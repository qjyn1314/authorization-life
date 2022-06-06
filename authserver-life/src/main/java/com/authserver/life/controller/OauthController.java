package com.authserver.life.controller;

import com.authserver.common.entity.UserDetail;
import com.authserver.common.entity.UserHelper;
import com.authserver.life.service.UserService;
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
@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Autowired
    private UserService userService;

    @GetMapping("/self-user")
    public UserDetail getCurrentUser() {
        return UserHelper.getUserDetail();
    }


}
