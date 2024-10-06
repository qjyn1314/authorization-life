package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.entity.LsysLov;
import com.authorization.life.system.app.service.LsysLovService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 字典主表
 *
 * @author code@code.com
 * @date 2024-10-05 19:21:32
 */
@Slf4j
@RestController
@RequestMapping("/lsys/lov" )
@Tag(name = "lsys/lov", description = "字典主表控制层")
public class LsysLovController {

    @Autowired
    private LsysLovService lsysLovService;



}
