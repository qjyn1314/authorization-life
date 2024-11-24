package com.authorization.life.auth.app.service;

import com.authorization.life.auth.app.dto.LsysMenuDTO;
import com.authorization.life.auth.app.vo.LsysMenuVO;
import com.authorization.life.auth.infra.entity.LsysMenu;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 菜单资源表
 *
 * @author code@code.com
 * @date 2024-11-24 16:29:24
 */
public interface LsysMenuService {

    PageInfo<LsysMenuVO> page(LsysMenuDTO lsysMenu);


    List<LsysMenuVO> listByParams(LsysMenuDTO lsysMenu);


    String save(LsysMenuDTO lsysMenu);


    LsysMenuVO detailById(LsysMenuDTO lsysMenu);


    LsysMenuVO detailByParams(LsysMenuDTO lsysMenu);


    String update(LsysMenuDTO lsysMenu);


    String delete(LsysMenuDTO lsysMenu);


}
