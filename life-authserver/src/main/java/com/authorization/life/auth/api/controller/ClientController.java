package com.authorization.life.auth.api.controller;


import com.authorization.life.auth.app.dto.OauthClientDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.utils.result.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理client信息
 *
 * @date 2025-03-28 14:45
 */
@Slf4j
@Tag(name = "管理client信息", description = "管理client信息")
@RestController
@RequestMapping("/client")
public class ClientController {

    @Resource
    private OauthClientService oauthClientService;

    @Operation(summary = "client信息列表")
    @PostMapping("/page")
    public Result<PageInfo<OauthClientVO>> page(@RequestBody OauthClientDTO clientDTO) {
        return Result.ok(oauthClientService.page(clientDTO));
    }


    @Operation(summary = "生成授权路径")
    @GetMapping("/genAuthorizationUrl")
    public Result<List<OauthClientVO>> genAuthorizationUrl(@RequestParam("clientId") String clientId,
                                                           @RequestParam("hostOrigin") String hostOrigin) {
        return Result.ok(oauthClientService.genAuthorizationUrl(clientId, hostOrigin));
    }


    @Operation(summary = "保存client信息")
    @PostMapping("/saveClient")
    public Result<OauthClientVO> saveClient(@RequestBody OauthClientDTO clientDTO) {
        return Result.ok(oauthClientService.saveClient(clientDTO));
    }


    @Operation(summary = "获取client详情")
    @GetMapping("/getClient")
    public Result<OauthClientVO> getClient(@RequestParam("clientId") String clientId) {
        return Result.ok(oauthClientService.getClient(clientId));
    }


    @Operation(summary = "获取client详情")
    @GetMapping("/delClient")
    public Result<Boolean> delClient(@RequestParam("clientId") String clientId) {
        return Result.ok(oauthClientService.delClient(clientId));
    }


}
