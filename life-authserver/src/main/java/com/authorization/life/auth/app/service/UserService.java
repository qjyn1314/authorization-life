package com.authorization.life.auth.app.service;

import com.authorization.core.entity.UserDetail;
import com.authorization.life.auth.entity.LifeUser;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
public interface UserService {


    /**
     * 登录查询使用
     *
     * @param username 手机号/邮箱
     */
    LifeUser selectByUsername(String username);

    /**
     * 创建当前登录用户信息
     *
     * @param principal security中的当前登录用户
     */
    UserDetail createUserDetailByUser(UserDetails principal);

    /**
     * 锁定用户几小时
     *
     * @param userId 用户ID
     * @param lockTime 锁定时间-单位：小时；
     */
    void lock(Long userId , Integer lockTime);

    PageInfo<LifeUser> page(LifeUser lifeUser);

}
