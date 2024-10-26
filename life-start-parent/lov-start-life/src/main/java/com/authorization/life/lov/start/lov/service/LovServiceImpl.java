package com.authorization.life.lov.start.lov.service;

import cn.hutool.core.collection.CollUtil;
import com.authorization.life.lov.start.lov.entity.LovDetail;
import com.authorization.life.lov.start.lov.entity.LovValueDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LovServiceImpl implements LovService {

    public static final List<LovValueDetail> lovValueList = CollUtil.newArrayList();
    public static final ConcurrentHashMap<String, LovDetail> lovMap = new ConcurrentHashMap<>();

    static {
        lovValueList.add(LovValueDetail.of("USER_SEX", "0", "未知"));
        lovValueList.add(LovValueDetail.of("USER_SEX", "1", "男"));
        lovValueList.add(LovValueDetail.of("USER_SEX", "2", "女"));
        lovMap.put("USER_SEX", LovDetail.of("USER_SEX", "用户性别"));
    }

    @Override
    public LovDetail selectLov(Long tenantId, String lovCode) {
        return lovMap.get(lovCode);
    }

    @Override
    public List<LovValueDetail> selectLovValue(Long tenantId, String lovCode) {
        return lovValueList;
    }


}
