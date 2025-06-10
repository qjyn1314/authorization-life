package com.authorization.life.system.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.system.app.dto.LsysTempDTO;
import com.authorization.life.system.app.service.LsysTempService;
import com.authorization.life.system.app.vo.LsysTempVO;
import com.authorization.life.system.infra.entity.LsysTemp;
import com.authorization.life.system.infra.mapper.LsysTempMapper;
import com.authorization.remote.system.vo.TempVO;
import com.authorization.valid.start.group.SaveGroup;
import com.authorization.valid.start.group.UpdateGroup;
import com.authorization.valid.start.util.ValidUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
                .map(lsysTemp -> Convert.convert(TempVO.class, lsysTemp)).orElse(null);
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
        Assert.notNull(sysTemp.getPageNo(), "第几页不能为空.");
        Assert.notNull(sysTemp.getPageSize(), "每页显示记录数不能为空.");
        return PageHelper.startPage(sysTemp.getPageNo(), sysTemp.getPageSize()).doSelectPageInfo(() -> {
            handleTempList(sysTemp);
        });
    }

    private List<LsysTempVO> handleTempList(LsysTempDTO sysTemp) {
        LambdaQueryWrapper<LsysTemp> queryWrapper = Wrappers.lambdaQuery(new LsysTemp());

        queryWrapper.likeRight(StrUtil.isNotBlank(sysTemp.getTempCode()), LsysTemp::getTempCode, sysTemp.getTempCode());
        queryWrapper.likeRight(StrUtil.isNotBlank(sysTemp.getTempDesc()), LsysTemp::getTempDesc, sysTemp.getTempDesc());
        queryWrapper.eq(StrUtil.isNotBlank(sysTemp.getTempType()), LsysTemp::getTempType, sysTemp.getTempType());
        queryWrapper.orderByDesc(LsysTemp::getCreatedTime);
        List<LsysTemp> lsysTemps = mapper.selectList(queryWrapper);
        return lsysTemps.stream()
                .map(item -> Convert.convert(LsysTempVO.class, item))
                .collect(Collectors.toList());
    }

    @Override
    public LsysTempVO tempByParams(LsysTempDTO sysTemp) {
        LambdaQueryWrapper<LsysTemp> queryWrapper = Wrappers.lambdaQuery(new LsysTemp());
        queryWrapper.eq(StrUtil.isNotBlank(sysTemp.getTempId()), LsysTemp::getTempId, sysTemp.getTempId());
        queryWrapper.eq(StrUtil.isNotBlank(sysTemp.getTempCode()), LsysTemp::getTempCode, sysTemp.getTempCode());
        List<LsysTemp> lsysTemps = mapper.selectList(queryWrapper);
        return lsysTemps.stream().findFirst()
                .map(item -> Convert.convert(LsysTempVO.class, item)).orElse(new LsysTempVO());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateTemp(LsysTempDTO sysTemp) {
        ValidUtil.validateAndThrow(sysTemp, UpdateGroup.class);
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysTemp())
                .notIn(LsysTemp::getTempId, sysTemp.getTempId())
                .eq(LsysTemp::getTempCode, sysTemp.getTempCode()));
        Assert.isFalse(selectTempCodeCount > 0, "模板编码已存在.");
        LsysTemp lsysTempUpdate = new LsysTemp();
        BeanUtils.copyProperties(sysTemp, lsysTempUpdate);
        mapper.updateById(lsysTempUpdate);
        return lsysTempUpdate.getTempId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteTemp(LsysTempDTO sysTemp) {
        Assert.notBlank(sysTemp.getTempId(), "主键ID不能为空.");
        LsysTemp lsysTemp = mapper.selectById(sysTemp.getTempId());
        Assert.notNull(lsysTemp, "未找到相应的模板信息.");
        Assert.isFalse(lsysTemp.getEnabledFlag(), "模板已启用, 不能删除.");
        mapper.deleteById(sysTemp.getTempId());
        return sysTemp.getTempId();
    }

}
