package com.authorization.life.service.impl;

import cn.hutool.core.lang.Assert;
import com.authorization.core.entity.UserDetail;
import com.authorization.core.entity.UserHelper;
import com.authorization.life.service.UserService;
import com.authorization.life.entity.User;
import com.authorization.life.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
     *
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

    /**
     * 锁定用户几小时
     *
     * @param userId   用户ID
     * @param lockTime 锁定时间-单位：小时；
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lock(Long userId, Integer lockTime) {
        User user = mapper.selectOne(Wrappers.lambdaQuery(new User()).eq(User::getUserId, userId));
        Assert.notNull(user, "未找到该用户信息");
        UserHelper.setUserDetail(UserDetail.systemUser());
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate(new User())
                .eq(User::getUserId, user)
                .set(User::getLockedFlag, Boolean.TRUE)
                .set(User::getLockedTime, LocalDateTime.now().plusHours(lockTime));
        mapper.update(user, updateWrapper);
        UserHelper.setUserDetail(null);
    }
}
