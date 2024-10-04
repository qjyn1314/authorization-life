package com.authorization.life.system.api.controller;

import com.authorization.life.system.app.service.LsysTempUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 模板接收人员
 *
 * @author code@code.com
 * @date 2024-10-01 23:08:40
 */
@Slf4j
@RestController
@RequestMapping("/lsys/temp/user" )
@Tag(name = "lsys/temp/user", description = "模板接收人员控制层")
public class LsysTempUserController {

    @Autowired
    private LsysTempUserService lsysTempUserService;



}
