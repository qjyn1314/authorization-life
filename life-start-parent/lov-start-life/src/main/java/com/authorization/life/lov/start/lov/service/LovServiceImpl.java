package com.authorization.life.lov.start.lov.service;

import cn.hutool.core.collection.CollUtil;
import com.authorization.life.lov.start.lov.entity.LovDetail;
import com.authorization.life.lov.start.lov.entity.LovValueDetail;
import com.authorization.remote.system.SystemRemoteRes;
import com.authorization.remote.system.service.SystemRemoteService;
import com.authorization.remote.system.vo.LsysLovRemoteVO;
import com.authorization.remote.system.vo.LsysLovValueRemoteVO;
import com.authorization.utils.converter.BeanConverter;
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
        return BeanConverter.convert(remoteRes.getData(), LovDetail.class);
    }

    @Override
    public List<LovValueDetail> selectLovValue(String tenantId, String lovCode) {
        SystemRemoteRes<List<LsysLovValueRemoteVO>> remoteRes = systemRemoteService.selectLovValue(tenantId, lovCode);
        if (CollUtil.isEmpty(remoteRes.getData())) {
            return null;
        }
        return remoteRes.getData().stream().map(item -> BeanConverter.convert(item, LovValueDetail.class)).collect(Collectors.toList());
    }


}
