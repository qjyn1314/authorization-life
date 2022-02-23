package com.authserver.life.service.impl;

import com.authserver.life.entity.User;
import com.authserver.life.mapper.UserMapper;
import com.authserver.life.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;


    @Override
    public User selectByUsername(String username) {
        return mapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getUsername,username));
    }
}
