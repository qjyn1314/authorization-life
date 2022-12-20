package com.authorization.gateway.knife4j;

import lombok.Getter;
import lombok.Setter;

/**
 * 文档路由信息类
 *
 * @author wangjunming
 * @date 2022/12/20 20:45
 */
@Setter
@Getter
public class Knife4jGatewayRoute {

    /**
     * 分组名称,例如：用户服务
     */
    private String name;

    /**
     * 服务名称,例如：user-service
     */
    private String serviceName;
    /**
     * 自服务加载url地址,例如：/v2/api-docs?group=default
     */
    private String url;

}