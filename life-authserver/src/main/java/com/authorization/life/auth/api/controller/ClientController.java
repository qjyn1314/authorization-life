package com.authorization.life.auth.api.controller;


import com.authorization.life.auth.app.dto.OauthClientDTO;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.AuthorizationGrant;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.utils.result.Result;
import com.authorization.valid.start.util.ValidUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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


    @Operation(summary = "生成授权路径")
    @GetMapping("/genGrantTypeUrl")
    public Result<List<AuthorizationGrant>> genGrantTypeUrl(@RequestParam("clientId") String clientId) {
        return Result.ok(oauthClientService.genGrantTypeUrl(clientId));
    }


    @Operation(summary = "保存client信息")
    @PostMapping("/saveClient")
    public Result<OauthClientVO> saveClient(@RequestBody OauthClientDTO clientDTO) {
        ValidUtil.validateAndThrow(clientDTO);
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


    @Operation(summary = "获取下拉框的选项值")
    @GetMapping("/getSelectVal")
    public Result<Map<String, LinkedList<String>>> getSelectVal() {
        Map<String, LinkedList<String>> result = new LinkedHashMap<>();

        // 授权模式
        LinkedList<String> grantTypes = new LinkedList<>();
        grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN.getValue());
        grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
//        grantTypes.add(AuthorizationGrantType.PASSWORD.getValue());
        grantTypes.add(AuthorizationGrantType.JWT_BEARER.getValue());
        grantTypes.add(AuthorizationGrantType.DEVICE_CODE.getValue());
        grantTypes.add(AuthorizationGrantType.TOKEN_EXCHANGE.getValue());
        result.put("grantType", grantTypes);

        // 授权域
        LinkedList<String> scopeMaps = new LinkedList<>();
        scopeMaps.add("user_info");
        scopeMaps.add(OidcScopes.OPENID);
        scopeMaps.add(OidcScopes.PROFILE);
        scopeMaps.add(OidcScopes.EMAIL);
        scopeMaps.add(OidcScopes.ADDRESS);
        scopeMaps.add(OidcScopes.PHONE);
        result.put("scope", scopeMaps);

        return Result.ok(result);
    }


}
