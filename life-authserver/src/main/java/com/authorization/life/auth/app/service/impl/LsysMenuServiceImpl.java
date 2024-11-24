package com.authorization.life.auth.app.service.impl;

import cn.hutool.core.lang.Assert;
import com.authorization.life.auth.app.dto.LsysMenuDTO;
import com.authorization.life.auth.app.service.LsysMenuService;
import com.authorization.life.auth.app.vo.LsysMenuVO;
import com.authorization.life.auth.infra.entity.LsysMenu;
import com.authorization.life.auth.infra.mapper.LsysMenuMapper;
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
 * 菜单资源表
 *
 * @author code@code.com
 * @date 2024-11-24 16:29:24
 */
@Service
public class LsysMenuServiceImpl implements LsysMenuService {

    @Autowired
    private LsysMenuMapper mapper;

    @Override
    public PageInfo<LsysMenuVO> page(LsysMenuDTO lsysMenu) {
        Assert.notNull(lsysMenu.getPageNo(), "第几页不能为空.");
        Assert.notNull(lsysMenu.getPageSize(), "每页显示记录数不能为空.");
        return PageHelper.startPage(lsysMenu.getPageNo(), lsysMenu.getPageSize())
                .doSelectPageInfo(() -> {
                    listByParams(lsysMenu);
                });
    }

    @Override
    public List<LsysMenuVO> listByParams(LsysMenuDTO lsysMenu) {
        List<LsysMenu> page = mapper.page(new LsysMenu());
        return page.stream().map(item -> BeanConverter.convert(item, LsysMenuVO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(LsysMenuDTO lsysMenu) {
        ValidUtil.validateAndThrow(lsysMenu, SaveGroup.class);
        LsysMenu lsysMenuInsert = new LsysMenu();
        BeanUtils.copyProperties(lsysMenu, lsysMenuInsert);
        mapper.insert(lsysMenuInsert);
        return lsysMenuInsert.getMenuId();
    }

    @Override
    public LsysMenuVO detailById(LsysMenuDTO lsysMenu) {
        LsysMenu item = mapper.selectById(lsysMenu.getMenuId());
        return BeanConverter.convert(item, LsysMenuVO.class);
    }

    @Override
    public LsysMenuVO detailByParams(LsysMenuDTO lsysMenu) {
        LambdaQueryWrapper<LsysMenu> queryWrapper = Wrappers.lambdaQuery(new LsysMenu());
        List<LsysMenu> dataList = mapper.selectList(queryWrapper);
        return dataList.stream().findFirst()
                .map(item -> BeanConverter.convert(item, LsysMenuVO.class)).orElse(new LsysMenuVO());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String update(LsysMenuDTO lsysMenu) {
        ValidUtil.validateAndThrow(lsysMenu, UpdateGroup.class);
        LsysMenu lsysMenuUpdate = new LsysMenu();
        BeanUtils.copyProperties(lsysMenu, lsysMenuUpdate);
        mapper.updateById(lsysMenuUpdate);
        return lsysMenuUpdate.getMenuId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String delete(LsysMenuDTO lsysMenuDTO) {
        Assert.notBlank(lsysMenuDTO.getMenuId(), "主键ID不能为空.");
        LsysMenu lsysMenu = mapper.selectById(lsysMenuDTO.getMenuId());
        Assert.notNull(lsysMenu, "数据库中未找到.");
        //删除主表的数据
        mapper.deleteById(lsysMenuDTO.getMenuId());
        return lsysMenuDTO.getMenuId();
    }


}
