package com.authorization.life.auth.api.controller;


import com.authorization.life.auth.app.dto.LsysMenuDTO;
import com.authorization.life.auth.app.service.LsysMenuService;
import com.authorization.life.auth.app.vo.LsysMenuVO;
import com.authorization.utils.result.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 菜单资源表
 *
 * @author code@code.com
 * @date 2024-11-24 16:29:24
 */
@Slf4j
@RestController
@RequestMapping("/lsys/menu")
@Tag(name = "lsys/menu", description = "菜单资源表控制层")
public class LsysMenuController {

    @Autowired
    private LsysMenuService lsysMenuService;

    @Operation(summary = "菜单资源表分页查询列表")
    @PostMapping("/page")
    public Result<PageInfo<LsysMenuVO>> page(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.page(lsysMenu));
    }

    @Operation(summary = "菜单资源表分页查询列表")
    @PostMapping("/list")
    public Result<List<LsysMenuVO>> list(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.listByParams(lsysMenu));
    }

    @Operation(summary = "菜单资源表保存")
    @PostMapping("/save")
    public Result<String> save(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.save(lsysMenu));
    }

    @Operation(summary = "菜单资源表详情ById")
    @PostMapping("/detailById")
    public Result<LsysMenuVO> detailById(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.detailById(lsysMenu));
    }

    @Operation(summary = "菜单资源表详情ByParams")
    @PostMapping("/detailByParams")
    public Result<LsysMenuVO> detailByParams(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.detailByParams(lsysMenu));
    }

    @Operation(summary = "菜单资源表更新")
    @PostMapping("/update")
    public Result<String> update(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.update(lsysMenu));
    }

    @Operation(summary = "菜单资源表删除")
    @PostMapping("/delete")
    public Result<String> delete(@RequestBody LsysMenuDTO lsysMenu) {
        return Result.ok(lsysMenuService.delete(lsysMenu));
    }


}
