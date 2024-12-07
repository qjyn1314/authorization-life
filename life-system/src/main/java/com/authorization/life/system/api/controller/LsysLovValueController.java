package com.authorization.life.system.api.controller;

import com.authorization.life.system.app.dto.LsysLovValueDTO;
import com.authorization.life.system.app.service.LsysLovValueService;
import com.authorization.life.system.app.vo.LsysLovValueVO;
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
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Slf4j
@RestController
@RequestMapping("/lsys/lov/value")
@Tag(name = "lsys/lov/value", description = "字典明细表控制层")
public class LsysLovValueController {

    @Autowired
    private LsysLovValueService lsysLovValueService;

    @Operation(summary = "值集明细主表分页")
    @PostMapping("/page")
    public Result<PageInfo<LsysLovValueVO>> page(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.page(lovValueDTO));
    }

    @Operation(summary = "值集明细与值集主表分页")
    @PostMapping("/lovValuePage")
    public Result<PageInfo<LsysLovValueVO>> lovValuePage(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.lovValuePage(lovValueDTO));
    }

    @Operation(summary = "保存值集")
    @PostMapping("/save-lov-value")
    public Result<String> saveLovValue(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.saveLovValue(lovValueDTO));
    }

    @Operation(summary = "值集详情")
    @PostMapping("/lov-value-one")
    public Result<LsysLovValueVO> lovValueByParams(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.lovValueByParams(lovValueDTO));
    }

    @Operation(summary = "更新值集")
    @PostMapping("/update-lov-value")
    public Result<String> updateLovValue(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.updateLovValue(lovValueDTO));
    }

    @Operation(summary = "删除值集")
    @PostMapping("/delete-lov-value")
    public Result<String> deleteLovValue(@RequestBody LsysLovValueDTO lovValueDTO) {
        return Result.ok(lsysLovValueService.deleteLovValue(lovValueDTO));
    }

    @Operation(summary = "删除值集")
    @PostMapping("/batchDeleteLovValue")
    public Result<String> batchDeleteLovValue(@RequestBody List<String> lovValueIds) {
        return Result.ok(lsysLovValueService.batchDeleteLovValue(lovValueIds));
    }


}
