package com.authorization.life.system.infra.service.impl;

import com.authorization.life.system.infra.entity.Emp;
import com.authorization.life.system.infra.mapper.EmpMapper;
import com.authorization.life.system.infra.service.EmpService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-06-23 14:38:43
 */
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper mapper;



}
