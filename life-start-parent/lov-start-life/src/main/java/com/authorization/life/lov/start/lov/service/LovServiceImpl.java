package com.authorization.life.lov.start.lov.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.authorization.life.lov.start.lov.entity.LovDetail;
import com.authorization.life.lov.start.lov.entity.LovValueDetail;
import com.authorization.remote.system.SystemRemoteRes;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.remote.system.vo.LsysLovRemoteVO;
import com.authorization.remote.system.vo.LsysLovValueRemoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LovServiceImpl implements LovService {

    @Autowired
    private SystemRemoteService systemRemoteService;

    @Override
    public LovDetail selectLov(String tenantId, String lovCode) {
        SystemRemoteRes<LsysLovRemoteVO> remoteRes = systemRemoteService.selectLov(tenantId, lovCode);
        if (Objects.isNull(remoteRes.getData())) {
            return null;
        }
        return Convert.convert(LovDetail.class, remoteRes.getData());
    }

    @Override
    public List<LovValueDetail> selectLovValue(String tenantId, String lovCode) {
        SystemRemoteRes<List<LsysLovValueRemoteVO>> remoteRes = systemRemoteService.selectLovValue(tenantId, lovCode);
        if (CollUtil.isEmpty(remoteRes.getData())) {
            return null;
        }
        return remoteRes.getData().stream().map(item ->
                Convert.convert(LovValueDetail.class, remoteRes.getData())
        ).collect(Collectors.toList());
    }


}
