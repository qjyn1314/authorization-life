package com.authserver.life.service.impl;

import com.authserver.life.entity.UserGroup;
import com.authserver.life.mapper.UserGroupMapper;
import com.authserver.life.service.UserGroupService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户组表
 *
 * @author code@code.com
 * @date 2022-02-21 20:24:00
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupMapper mapper;


    /**
     * 查找该用户所属的用户组
     */
    @Override
    public List<UserGroup> selectByUserId(Long userId) {
        return mapper.selectList(Wrappers.lambdaQuery(UserGroup.class).eq(UserGroup::getUserId, userId));
    }
}
