package com.authorization.life.system.api.controller;

import com.authorization.life.system.app.dto.LsysLovDTO;
import com.authorization.life.system.app.service.LsysLovService;
import com.authorization.life.system.app.vo.LsysLovVO;
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


/**
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Slf4j
@RestController
@RequestMapping("/lsys/lov")
@Tag(name = "lsys/lov", description = "值集主表控制层")
public class LsysLovController {

    @Autowired
    private LsysLovService lsysLovService;

    @Operation(summary = "值集主表分页")
    @PostMapping("/page")
    public Result<PageInfo<LsysLovVO>> page(@RequestBody LsysLovDTO lovDTO) {
        return Result.ok(lsysLovService.page(lovDTO));
    }

    @Operation(summary = "保存值集")
    @PostMapping("/save-lov")
    public Result<String> saveLov(@RequestBody LsysLovDTO sysLov) {
        return Result.ok(lsysLovService.saveLov(sysLov));
    }

    @Operation(summary = "值集详情")
    @PostMapping("/lov-one")
    public Result<LsysLovVO> lovByParams(@RequestBody LsysLovDTO sysLov) {
        return Result.ok(lsysLovService.lovByParams(sysLov));
    }

    @Operation(summary = "更新值集")
    @PostMapping("/update-lov")
    public Result<String> updateLov(@RequestBody LsysLovDTO sysLov) {
        return Result.ok(lsysLovService.updateLov(sysLov));
    }

    @Operation(summary = "删除值集")
    @PostMapping("/delete-lov")
    public Result<String> deleteLov(@RequestBody LsysLovDTO sysLov) {
        return Result.ok(lsysLovService.deleteLov(sysLov));
    }


}
