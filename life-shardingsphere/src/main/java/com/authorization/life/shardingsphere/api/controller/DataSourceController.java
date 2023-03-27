package com.authorization.life.shardingsphere.api.controller;

import com.authorization.life.shardingsphere.app.service.ConfDatasourceService;
import com.authorization.life.shardingsphere.infra.entity.ConfDatasource;
import com.authorization.utils.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 多数据源控制类-
 *
 * @author wangjunming
 * @date 2023/2/24 13:26
 */
@Tag(name = "动态数据源控制层", description = "动态数据源控制层")
@RestController
@RequestMapping("/v1/datasource")
public class DataSourceController {

    @Autowired
    private ConfDatasourceService datasourceService;

    @Operation(summary = "测试动态数据源手动切换的效果")
    @GetMapping("/query/service-name/{serviceName}")
    public Result<List<ConfDatasource>> clientByDomain(@Parameter(description = "服务名称", example = "auth-life", required = true)
                                                       @PathVariable String serviceName) {
        List<ConfDatasource> confDatasources = datasourceService.selectDateSourceByServiceName(serviceName);
        return Result.ok(confDatasources);
    }

}
