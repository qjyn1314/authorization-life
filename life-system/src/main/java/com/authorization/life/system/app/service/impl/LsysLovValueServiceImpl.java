package com.authorization.life.system.app.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.system.app.dto.LsysLovValueDTO;
import com.authorization.life.system.app.service.LsysLovValueService;
import com.authorization.life.system.app.vo.LsysLovValueVO;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Service
public class LsysLovValueServiceImpl implements LsysLovValueService {

    @Autowired
    private LsysLovValueMapper mapper;
    @Autowired
    private LsysLovMapper lovMapper;

    @Override
    public PageInfo<LsysLovValueVO> page(LsysLovValueDTO lovValueDTO) {
        return PageHelper.startPage(lovValueDTO.getPageNum(), lovValueDTO.getPageSize())
                .doSelectPageInfo(() -> {
                    listByParams(lovValueDTO);
                });
    }

    @Override
    public List<LsysLovValueVO> listByParams(LsysLovValueDTO lovValueDTO) {
        Assert.notBlank(lovValueDTO.getLovId(), "值集主表Id不能为空.");
        LambdaQueryWrapper<LsysLovValue> queryWrapper = Wrappers.lambdaQuery(new LsysLovValue())
                .eq(LsysLovValue::getLovId, lovValueDTO.getLovId())
                .orderByDesc(LsysLovValue::getValueOrder)
                .orderByDesc(LsysLovValue::getCreatedTime);
        return mapper.selectList(queryWrapper).stream().map(item -> BeanConverter.convert(item, LsysLovValueVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String saveLovValue(LsysLovValueDTO lovValueDTO) {
        ValidUtil.validateAndThrow(lovValueDTO, SaveGroup.class);
        LsysLov lsysLov = lovMapper.selectById(lovValueDTO.getLovId());
        Assert.notNull(lsysLov, "未找到对应的值集主信息");
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysLovValue())
                .eq(LsysLovValue::getLovId, lovValueDTO.getLovId())
                .eq(LsysLovValue::getValueCode, lovValueDTO.getValueCode()));
        Assert.isFalse(selectTempCodeCount > 0, "值代码已存在.");
        LsysLovValue lsysLovValueInsert = new LsysLovValue();
        BeanUtils.copyProperties(lovValueDTO, lsysLovValueInsert);
        mapper.insert(lsysLovValueInsert);
        return lsysLovValueInsert.getLovValueId();
    }

    @Override
    public LsysLovValueVO lovValueByParams(LsysLovValueDTO lovValueDTO) {
        Assert.notBlank(lovValueDTO.getLovValueId(), "主键Id不能为空.");
        LambdaQueryWrapper<LsysLovValue> queryWrapper = Wrappers.lambdaQuery(new LsysLovValue());
        queryWrapper.eq(StrUtil.isNotBlank(lovValueDTO.getLovValueId()), LsysLovValue::getLovValueId, lovValueDTO.getLovValueId());
        List<LsysLovValue> lsysTemps = mapper.selectList(queryWrapper);
        return lsysTemps.stream().findFirst()
                .map(item -> BeanConverter.convert(item, LsysLovValueVO.class)).orElse(new LsysLovValueVO());
    }

    @Override
    public String updateLovValue(LsysLovValueDTO lovValueDTO) {
        ValidUtil.validateAndThrow(lovValueDTO, UpdateGroup.class);
        LsysLov lsysLov = lovMapper.selectById(lovValueDTO.getLovId());
        Assert.notNull(lsysLov, "未找到对应的值集主信息");
        Long selectTempCodeCount = mapper.selectCount(Wrappers.lambdaQuery(new LsysLovValue())
                .notIn(LsysLovValue::getLovValueId, lovValueDTO.getLovValueId())
                .eq(LsysLovValue::getLovId, lovValueDTO.getLovId())
                .eq(LsysLovValue::getValueCode, lovValueDTO.getValueCode()));
        Assert.isFalse(selectTempCodeCount > 0, "值代码已存在.");
        LsysLovValue lsysLovValueUpdate = new LsysLovValue();
        BeanUtils.copyProperties(lovValueDTO, lsysLovValueUpdate);
        mapper.updateById(lsysLovValueUpdate);
        return lsysLovValueUpdate.getLovValueId();
    }

    @Override
    public String deleteLovValue(LsysLovValueDTO lovValueDTO) {
        Assert.notBlank(lovValueDTO.getLovValueId(), "主键ID不能为空.");
        LsysLovValue lsysLovValue = mapper.selectById(lovValueDTO.getLovValueId());
        Assert.notNull(lsysLovValue, "未找到相应的值代码信息.");
        Assert.isFalse(lsysLovValue.getEnabledFlag(), "值代码已启用, 不能删除.");
        mapper.deleteById(lsysLovValue.getLovValueId());
        return lsysLovValue.getLovValueId();
    }
}
