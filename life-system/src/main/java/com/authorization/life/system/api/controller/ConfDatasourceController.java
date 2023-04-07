package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.service.ConfDatasourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 数据源表
 *
 * @author code@code.com
 * @date 2023-04-07 18:00:09
 */
@Slf4j
@RestController
@RequestMapping("/conf/datasource")
@Tag(name = "conf/datasource", description = "数据源表控制层")
public class ConfDatasourceController {

    @Autowired
    private ConfDatasourceService confDatasourceService;


}
