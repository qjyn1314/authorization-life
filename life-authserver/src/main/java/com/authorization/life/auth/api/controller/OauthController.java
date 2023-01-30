package com.authorization.life.auth.api.controller;

import com.authorization.core.entity.UserDetail;
import com.authorization.core.entity.UserHelper;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.service.UserService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.utils.result.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录控制层
 *
 * @author code@code.com
 * @date 2022-02-21 20:21:01
 */
@Tag(name = "登录控制层", description = "获取当前登录用户信息,权限信息")
@RestController
@RequestMapping("/v1/oauth")
public class OauthController {

    @Autowired
    private UserService userService;

    @Autowired
    private OauthClientService oauthClientService;

    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/self-user")
    public R<UserDetail> getCurrentUser() {
        return R.ok(UserHelper.getUserDetail());
    }

    @Operation(summary = "通过请求的域名获取client信息.")
    @GetMapping("/client-domain/{domainName}")
    public R<OauthClientVO> clientByDomain(@Parameter(description = "请求路径中的域名", example = "www.authorization.life", required = true)
                                           @PathVariable String domainName) {
        return R.ok(oauthClientService.clientByDomain(domainName));
    }


}
