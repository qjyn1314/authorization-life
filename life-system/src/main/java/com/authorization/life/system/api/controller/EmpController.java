package com.authorization.life.system.api.controller;

import com.authorization.life.system.infra.service.EmpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 员工表
 *
 * @author code@code.com
 * @date 2023-06-23 14:38:43
 */
@Slf4j
@RestController
@RequestMapping("/lemd/emp")
@Tag(name = "lemd/emp", description = "员工表控制层")
public class EmpController {

    @Autowired
    private EmpService lemdEmpService;


}
