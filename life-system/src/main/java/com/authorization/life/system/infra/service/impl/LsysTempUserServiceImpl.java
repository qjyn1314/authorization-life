package com.authorization.life.system.infra.service.impl;

import com.authorization.life.system.infra.entity.LsysTempUser;
import com.authorization.life.system.infra.mapper.LsysTempUserMapper;
import com.authorization.life.system.infra.service.LsysTempUserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 模板接收人员
 *
 * @author code@code.com
 * @date 2024-10-01 23:08:40
 */
@Service
public class LsysTempUserServiceImpl implements LsysTempUserService {

    @Autowired
    private LsysTempUserMapper mapper;



}
