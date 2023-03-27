package com.authorization.remote.sharding.service;

import com.authorization.remote.sharding.vo.DatasourceVO;

import java.util.List;

/**
 * sharding-life 服务 暴露的远程接口
 *
 * @author wangjunming
 * @date 2022/12/23 14:17
 */
public interface ShardingRemoteService {


    List<DatasourceVO> selectDateSourceByServiceName(String serviceName);

}
