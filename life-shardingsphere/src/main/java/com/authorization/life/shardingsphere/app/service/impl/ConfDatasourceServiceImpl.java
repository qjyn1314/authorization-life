package com.authorization.life.shardingsphere.app.service.impl;


import cn.hutool.core.lang.Assert;
import com.authorization.life.shardingsphere.app.service.ConfDatasourceService;
import com.authorization.life.shardingsphere.infra.entity.ConfDatasource;
import com.authorization.life.shardingsphere.infra.mapper.ConfDatasourceMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-02-24 13:29:22
 */
@Service
public class ConfDatasourceServiceImpl implements ConfDatasourceService {

    @Autowired
    private ConfDatasourceMapper mapper;

    @Override
    public List<ConfDatasource> selectDateSourceByServiceName(String serviceName) {
        Assert.notBlank(serviceName, "服务名称不能为空.");
        return mapper.selectList(Wrappers.query(new ConfDatasource().setServiceName(serviceName)));
    }
}
