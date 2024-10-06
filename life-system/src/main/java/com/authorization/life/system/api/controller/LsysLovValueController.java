package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.entity.LsysLovValue;
import com.authorization.life.system.app.service.LsysLovValueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 字典明细表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:43
 */
@Slf4j
@RestController
@RequestMapping("/lsys/lov/value" )
@Tag(name = "lsys/lov/value", description = "字典明细表控制层")
public class LsysLovValueController {

    @Autowired
    private LsysLovValueService lsysLovValueService;



}
