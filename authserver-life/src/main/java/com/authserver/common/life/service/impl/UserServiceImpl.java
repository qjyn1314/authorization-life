package com.authserver.common.life.service.impl;

import com.authserver.common.life.entity.User;
import com.authserver.common.life.entity.UserDetail;
import com.authserver.common.life.mapper.UserMapper;
import com.authserver.common.life.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    /**
     * 登录查询使用
     * @param username 手机号/邮箱
     */
    @Override
    public User selectByUsername(String username) {
        return mapper.selectOne(Wrappers.lambdaQuery(User.class)
                .or().eq(User::getPhone, username)
                .or().eq(User::getEmail, username));
    }

    /**
     * 创建当前登录用户信息
     *
     * @param principal security中的当前登录用户
     */
    @Override
    public UserDetail createUserDetailByUser(UserDetails principal) {

        return null;
    }
}
