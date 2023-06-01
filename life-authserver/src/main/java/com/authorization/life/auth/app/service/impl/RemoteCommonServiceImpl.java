package com.authorization.life.auth.app.service.impl;

import com.authorization.common.log.LogAdvice;
import com.authorization.life.auth.app.service.RemoteCommonService;
import com.authorization.remote.system.SystemServerApiRes;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.utils.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * dubbo接口公共集成接口的实现, 将包含所有在此项目中调用的dubbo接口.
 *
 * @author wangjunming
 * @date 2023/3/31 14:12
 */
@Service
public class RemoteCommonServiceImpl implements RemoteCommonService {

    @Resource
    private SystemRemoteService systemRemoteService;

    @LogAdvice(name = "获取-SystemRemote-当前时间")
    @Override
    public String getSystemRemoteNowDate() {
        SystemServerApiRes<String> systemRemoteNowDate = systemRemoteService.getSystemRemoteNowDate();
        return systemRemoteNowDate.getData();
    }

}