package com.authserver.life.controller;

import com.authserver.life.entity.User;
import com.authserver.life.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录控制层
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@RestController
@RequestMapping("/oauth" )
public class OauthLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public void login(){
       User user =  userService.selectByUsername("zhangsan");
    }


}
