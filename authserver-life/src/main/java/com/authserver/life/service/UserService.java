package com.authserver.life.service;

import com.authserver.life.entity.User;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
public interface UserService {


    User selectByUsername(String username);
}
