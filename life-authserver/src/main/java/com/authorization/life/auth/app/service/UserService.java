package com.authorization.life.auth.app.service;

import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.vo.LifeUserVO;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.github.pagehelper.PageInfo;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
public interface UserService {


    PageInfo<LifeUserVO> page(LifeUserDTO lifeUser);

    /**
     * 登录查询使用
     *
     * @param username 手机号/邮箱
     */
    LifeUser selectByUsername(String username);

    /**
     * 锁定用户几小时
     *
     * @param userId   用户ID
     * @param lockTime 锁定时间-单位：小时；
     */
    void lock(String userId, Integer lockTime);

    /**
     * 注册用户
     *
     * @param lifeUser 用户信息
     * @return String-> 用户编码
     */
    String emailRegister(LifeUserDTO lifeUser);

    /**
     * 验证邮箱是否存在
     *
     * @param userDTO 用户注册邮箱
     * @return Boolean
     */
    Boolean validateEmailExist(LifeUserDTO userDTO);

    /**
     * 重置密码
     *
     * @param lifeUser 用户邮箱信息
     */
    void resetPassword(LifeUserDTO lifeUser);

}
