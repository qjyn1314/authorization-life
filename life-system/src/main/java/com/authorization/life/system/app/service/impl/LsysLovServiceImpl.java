package com.authorization.life.system.app.service.impl;

import com.authorization.life.system.app.dto.LsysLovDTO;
import com.authorization.life.system.app.service.LsysLovService;
import com.authorization.life.system.app.vo.LsysLovVO;
import com.authorization.life.system.infra.entity.LsysLov;
import com.authorization.life.system.infra.mapper.LsysLovMapper;
import com.authorization.utils.converter.BeanConverter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public PageInfo<LsysLovVO> page(LsysLovDTO lovDTO) {
        return PageHelper.startPage(lovDTO.getPageNum(), lovDTO.getPageNum())
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

}
