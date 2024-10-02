package com.authorization.life.system.infra.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.authorization.life.system.infra.dto.LsysTempDTO;
import com.authorization.life.system.infra.entity.LsysTemp;
import com.authorization.life.system.infra.mapper.LsysTempMapper;
import com.authorization.life.system.infra.service.LsysTempService;
import com.authorization.life.system.infra.vo.LsysTempVO;
import com.authorization.remote.system.vo.TempVO;
import com.authorization.utils.converter.BeanConverter;
import com.authorization.valid.start.group.SaveGroup;
import com.authorization.valid.start.util.ValidUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模版
 *
 * @author code@code.com
 * @date 2024-10-01 23:07:37
 */
@Service
public class LsysTempServiceImpl implements LsysTempService {

    @Autowired
    private LsysTempMapper mapper;


    @Override
    public TempVO getTempByCode(String code) {
        Assert.notBlank(code, "模板编码不能为空.");
        List<LsysTemp> lsysTemps = mapper.selectList(Wrappers.lambdaQuery(new LsysTemp()).eq(LsysTemp::getTempCode, code));
        if (CollUtil.isEmpty(lsysTemps)) {
            return null;
        }
        return lsysTemps.stream()
                .findFirst()
                .map(lsysTemp -> BeanConverter.convert(lsysTemp, TempVO.class)).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveTemp(LsysTempDTO sysTemp) {
        ValidUtil.validateAndThrow(sysTemp, SaveGroup.class);
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysTemp())
                .eq(LsysTemp::getTempCode, sysTemp.getTempCode()));
        Assert.isFalse(selectTempCodeCount > 0, "模板编码已存在.");
        LsysTemp lsysTempInsert = new LsysTemp();
        BeanUtils.copyProperties(sysTemp, lsysTempInsert);
        mapper.insert(lsysTempInsert);
        return lsysTempInsert.getTempId();
    }

    @Override
    public PageInfo<LsysTempVO> pageTemp(LsysTempDTO sysTemp) {
        return PageHelper.startPage(sysTemp.getPageNum(), sysTemp.getPageSize()).doSelectPageInfo(() -> {
            handleTempList(sysTemp);
        });
    }

    private List<LsysTempVO> handleTempList(LsysTempDTO sysTemp) {
        List<LsysTemp> lsysTemps = mapper.selectList(Wrappers.lambdaQuery(new LsysTemp()));
        return lsysTemps.stream()
                .map(item -> BeanConverter.convert(item, LsysTempVO.class))
                .collect(Collectors.toList());
    }


}
