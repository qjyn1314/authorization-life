package com.authorization.life.system.app.service.impl;

import com.authorization.life.system.infra.entity.LsysLovValue;
import com.authorization.life.system.infra.mapper.LsysLovValueMapper;
import com.authorization.life.system.app.service.LsysLovValueService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Service
public class LsysLovValueServiceImpl implements LsysLovValueService {

    @Autowired
    private LsysLovValueMapper mapper;



}
