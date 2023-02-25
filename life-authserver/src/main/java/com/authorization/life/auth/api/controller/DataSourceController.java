package com.authorization.life.auth.api.controller;

import com.authorization.life.auth.app.service.ConfDatasourceService;
import com.authorization.life.auth.app.service.OauthClientService;
import com.authorization.life.auth.app.vo.OauthClientVO;
import com.authorization.utils.result.R;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多数据源控制类-
 *
 * @author wangjunming
 * @date 2023/2/24 13:26
 */
@Tag(name = "登录管理控制层", description = "登录管理控制层")
@RestController
@RequestMapping("/v1/dynamic-datasource")
public class DataSourceController {

    @Autowired
    private ConfDatasourceService datasourceService;
    @Autowired
    private OauthClientService oauthClientService;


    @Operation(summary = "通过请求的域名获取client信息.")
    @GetMapping("/client-domain/{domainName}/grant-type/{grantType}")
    public R<OauthClientVO> clientByDomain(@Parameter(description = "请求路径中的域名", example = "www.authorization.life", required = true)
                                           @PathVariable String domainName,
                                           @Parameter(description = "请求路径中的授权类型", example = "authorization_code", required = true)
                                           @PathVariable String grantType) {

        DynamicDataSourceContextHolder.push("slave01");
        OauthClientVO oauthClientVO = oauthClientService.clientByDomain(domainName, grantType);
        DynamicDataSourceContextHolder.clear();

        return R.ok(oauthClientVO);
    }


}
