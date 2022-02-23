package com.authserver.life.service.impl;

import com.authserver.life.mapper.UserGroupMapper;
import com.authserver.life.service.UserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

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



}
