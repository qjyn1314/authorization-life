package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.lang.Assert;
import com.authorization.life.auth.app.dto.LifeUserDTO;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.LifeUserVO;
import com.authorization.life.auth.infra.entity.LifeUser;
import com.authorization.life.auth.infra.mapper.UserMapper;
import com.authorization.utils.converter.BeanConverter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author code@code.com
 * @date 2022-02-21 20:23:16
 */
@Slf4j
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
    public LifeUser selectByUsername(String username) {
        return mapper.selectOne(Wrappers.lambdaQuery(LifeUser.class)
                .or().eq(LifeUser::getPhone, username)
                .or().eq(LifeUser::getEmail, username));
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
        LifeUser lifeUser = mapper.selectOne(Wrappers.lambdaQuery(new LifeUser()).eq(LifeUser::getUserId, userId));
        Assert.notNull(lifeUser, "未找到该用户信息");
        LambdaUpdateWrapper<LifeUser> updateWrapper = Wrappers.lambdaUpdate(new LifeUser())
                .eq(LifeUser::getUserId, lifeUser)
                .set(LifeUser::getLockedFlag, Boolean.TRUE)
                .set(LifeUser::getLockedTime, LocalDateTime.now().plusHours(lockTime));
        mapper.update(lifeUser, updateWrapper);
    }

    @Override
    public PageInfo<LifeUserVO> page(LifeUserDTO lifeUser) {
        return PageHelper.startPage(lifeUser.getPageNum(), lifeUser.getPageSize())
                .doSelectPageInfo(() -> handleUserPage(lifeUser));
    }

    private List<LifeUserVO> handleUserPage(LifeUserDTO lifeUser) {
        List<LifeUser> userList = mapper.page(BeanConverter.convert(lifeUser, LifeUser.class));
        return userList.stream().map(item -> BeanConverter.convert(item, LifeUserVO.class)).collect(Collectors.toList());
    }

}
