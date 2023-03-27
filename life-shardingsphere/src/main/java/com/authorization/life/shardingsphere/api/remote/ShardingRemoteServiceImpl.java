package com.authorization.life.shardingsphere.api.remote;

import cn.hutool.core.lang.Assert;
import com.authorization.life.shardingsphere.app.service.ConfDatasourceService;
import com.authorization.life.shardingsphere.infra.entity.ConfDatasource;
import com.authorization.remote.sharding.service.ShardingRemoteService;
import com.authorization.remote.sharding.vo.DatasourceVO;
import com.authorization.utils.converter.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangjunming
 * @date 2023/3/27 17:53
 */
@Service
public class ShardingRemoteServiceImpl implements ShardingRemoteService {

    @Autowired
    private ConfDatasourceService datasourceService;

    @Override
    public List<DatasourceVO> selectDateSourceByServiceName(String serviceName) {
        Assert.notBlank(serviceName, "服务名称不能为空. ");
        List<ConfDatasource> confDatasources = datasourceService.selectDateSourceByServiceName(serviceName);
        return confDatasources.stream().map(item -> BeanConverter.convert(item, DatasourceVO.class)).collect(Collectors.toList());
    }

}