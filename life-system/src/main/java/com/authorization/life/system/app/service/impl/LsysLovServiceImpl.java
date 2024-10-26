package com.authorization.life.system.app.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.system.app.dto.LsysLovDTO;
import com.authorization.life.system.app.service.LsysLovService;
import com.authorization.life.system.app.vo.LsysLovVO;
import com.authorization.life.system.infra.entity.LsysLov;
import com.authorization.life.system.infra.entity.LsysLovValue;
import com.authorization.life.system.infra.mapper.LsysLovMapper;
import com.authorization.life.system.infra.mapper.LsysLovValueMapper;
import com.authorization.utils.converter.BeanConverter;
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
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Service
public class LsysLovServiceImpl implements LsysLovService {

    @Autowired
    private LsysLovMapper mapper;
    @Autowired
    private LsysLovValueMapper lovValueMapper;


    @Override
    public PageInfo<LsysLovVO> page(LsysLovDTO lovDTO) {
        return PageHelper.startPage(lovDTO.getPageNum(), lovDTO.getPageSize())
                .doSelectPageInfo(() -> {
                    listByParams(lovDTO);
                });
    }

    @Override
    public List<LsysLovVO> listByParams(LsysLovDTO lovDTO) {
        List<LsysLov> page = mapper.page(new LsysLov());
        return page.stream().map(item -> BeanConverter.convert(item, LsysLovVO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveLov(LsysLovDTO sysLov) {
        ValidUtil.validateAndThrow(sysLov, SaveGroup.class);
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysLov())
                .eq(LsysLov::getLovCode, sysLov.getLovCode()));
        Assert.isFalse(selectTempCodeCount > 0, "值集编码已存在.");
        LsysLov lsysLovInsert = new LsysLov();
        BeanUtils.copyProperties(sysLov, lsysLovInsert);
        mapper.insert(lsysLovInsert);
        return lsysLovInsert.getLovId();
    }

    @Override
    public LsysLovVO lovByParams(LsysLovDTO sysLov) {
        LambdaQueryWrapper<LsysLov> queryWrapper = Wrappers.lambdaQuery(new LsysLov());
        queryWrapper.eq(StrUtil.isNotBlank(sysLov.getLovId()), LsysLov::getLovId, sysLov.getLovId());
        queryWrapper.eq(StrUtil.isNotBlank(sysLov.getLovCode()), LsysLov::getLovCode, sysLov.getLovCode());
        List<LsysLov> lsysTemps = mapper.selectList(queryWrapper);
        return lsysTemps.stream().findFirst()
                .map(item -> BeanConverter.convert(item, LsysLovVO.class)).orElse(new LsysLovVO());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateLov(LsysLovDTO sysLov) {
        ValidUtil.validateAndThrow(sysLov, UpdateGroup.class);
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysLov())
                .notIn(LsysLov::getLovId, sysLov.getLovId())
                .eq(LsysLov::getLovCode, sysLov.getLovCode()));
        Assert.isFalse(selectTempCodeCount > 0, "模板编码已存在.");
        LsysLov lsysLovUpdate = new LsysLov();
        BeanUtils.copyProperties(sysLov, lsysLovUpdate);
        mapper.updateById(lsysLovUpdate);
        return lsysLovUpdate.getLovId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteLov(LsysLovDTO sysLov) {
        Assert.notBlank(sysLov.getLovId(), "主键ID不能为空.");
        LsysLov lsysLov = mapper.selectById(sysLov.getLovId());
        Assert.notNull(lsysLov, "未找到相应的模板信息.");
        Assert.isFalse(lsysLov.getEnabledFlag(), "模板已启用, 不能删除.");
        //删除子表的数据
        LambdaQueryWrapper<LsysLovValue> queryLovValueWrapper = Wrappers.lambdaQuery(new LsysLovValue());
        queryLovValueWrapper.eq(LsysLovValue::getLovId, sysLov.getLovId());
        lovValueMapper.delete(queryLovValueWrapper);
        //删除主表的数据
        mapper.deleteById(lsysLov.getLovId());
        return lsysLov.getLovId();
    }

}
