package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.dto.EmpDTO;
import com.authorization.life.system.infra.entity.Emp;
import com.authorization.life.system.infra.service.EmpService;
import com.authorization.utils.result.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-04-24 14:08:47
 */
@Slf4j
@RestController
@RequestMapping("/lemd/emp")
@Tag(description = "lemd/emp", name = "员工表控制层")
public class EmpController {

    @Autowired
    private EmpService lemdEmpService;

    /**
     * 批量保存员工信息
     *
     * @param empList 前端传参用户信息
     * @return JsonResult
     */
    @Operation(summary = "批量保存员工信息")
    @PostMapping("/batchSave")
    public Result<Boolean> batchSaveEmp(@RequestBody List<Emp> empList) {
        return Result.ok(lemdEmpService.batchSaveEmp(empList));
    }

    /**
     * 分页员工信息
     *
     * @param empDto 前端传参查询参数
     * @return
     */
    @Operation(summary = "分页员工信息")
    @PostMapping("/page")
    public Result<PageInfo<Emp>> page(@RequestBody EmpDTO empDto) {
        return Result.ok(lemdEmpService.page(empDto));
    }

    /**
     * 批量删除员工信息
     *
     * @param empList 前端传参用户信息
     * @return JsonResult
     */
    @Operation(summary = "批量保存员工信息")
    @DeleteMapping("/batchDel")
    public Result<Boolean> batchDel(@RequestBody List<Emp> empList) {
        return Result.ok(lemdEmpService.batchDel(empList));
    }

    /**
     * 查询员工信息单个
     *
     * @param empDto 前端传参查询参数
     * @return
     */
    @Operation(summary = "分页员工信息")
    @PostMapping("/view")
    public Result view(@RequestBody EmpDTO empDto) {
        return Result.ok(lemdEmpService.view(empDto));
    }

    /**
     * 查询员工信息单个
     *
     * @param empDto 前端传参查询参数
     * @return
     */
    @Operation(summary = "分页员工信息")
    @PostMapping("/update")
    public Result update(@RequestBody EmpDTO empDto) {
        return Result.ok(lemdEmpService.update(empDto));
    }

}
