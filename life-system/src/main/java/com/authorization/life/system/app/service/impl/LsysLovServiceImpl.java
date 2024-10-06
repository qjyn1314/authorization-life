package com.authorization.life.system.app.service.impl;

import com.authorization.life.system.infra.entity.LsysLov;
import com.authorization.life.system.infra.mapper.LsysLovMapper;
import com.authorization.life.system.app.service.LsysLovService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Service
public class LsysLovServiceImpl implements LsysLovService {

    @Autowired
    private LsysLovMapper mapper;



}
