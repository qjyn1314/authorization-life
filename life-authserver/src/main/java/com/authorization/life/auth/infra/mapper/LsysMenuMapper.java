package com.authorization.life.auth.infra.mapper;

import java.util.List;

import com.authorization.life.auth.infra.entity.LsysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 菜单资源表 持久
 *
 * @author code@code.com 2024-11-24 16:29:24
 */
@Repository
public interface LsysMenuMapper extends BaseMapper<LsysMenu> {

    List<LsysMenu> page(LsysMenu lsysMenu);

}
