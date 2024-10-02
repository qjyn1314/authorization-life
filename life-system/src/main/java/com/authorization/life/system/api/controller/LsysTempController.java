package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.dto.LsysTempDTO;
import com.authorization.life.system.infra.service.LsysTempService;
import com.authorization.life.system.infra.vo.LsysTempVO;
import com.authorization.remote.system.vo.TempVO;
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
 * 模版
 *
 * @author code@code.com
 * @date 2024-10-01 23:07:37
 */
@Slf4j
@RestController
@RequestMapping("/lsys/temp")
@Tag(name = "lsys/temp", description = "模版控制层")
public class LsysTempController {

    @Autowired
    private LsysTempService lsysTempService;

    @Operation(summary = "保存模板")
    @PostMapping("/save-temp")
    public Result<String> saveTemp(@RequestBody LsysTempDTO sysTemp) {
        return Result.ok(lsysTempService.saveTemp(sysTemp));
    }

    @Operation(summary = "根据编码获取模板")
    @PostMapping("/get-temp-code")
    public Result<TempVO> getTempByCode(@RequestBody LsysTempDTO sysTemp) {
        return Result.ok(lsysTempService.getTempByCode(sysTemp.getTempCode()));
    }

    @Operation(summary = "模板分页列表")
    @PostMapping("/page-temp")
    public Result<PageInfo<LsysTempVO>> pageTemp(@RequestBody LsysTempDTO sysTemp) {
        return Result.ok(lsysTempService.pageTemp(sysTemp));
    }


}
