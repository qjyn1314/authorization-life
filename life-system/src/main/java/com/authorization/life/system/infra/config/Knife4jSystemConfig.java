package com.authorization.life.system.infra.config;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * System服务的文档配置类
 *
 * @author wangjunming
 * @date 2022/12/20 16:49
 */
@Configuration
public class Knife4jSystemConfig {

    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("x-order", RandomUtil.randomInt(0, 100));
                    tag.setExtensions(map);
                });
            }
            if (openApi.getPaths() != null) {
                openApi.addExtension("x-test123", "333");
                openApi.getPaths().addExtension("x-abb", RandomUtil.randomInt(1, 100));
            }
        };
    }

    @Bean
    public GroupedOpenApi userApi() {
        String[] paths = {"/**"};
        String[] packagedToMatch = {"com.authorization.life.system.api.controller"};
        return GroupedOpenApi.builder().group("用户模块").pathsToMatch(paths).addOperationCustomizer((operation, handlerMethod) ->
                operation.addParametersItem(new HeaderParameter()
                .name("groupCode").example("测试")
                .description("集团code").schema(new StringSchema()
                        ._default("BR").name("groupCode").description("集团code")))).packagesToScan(packagedToMatch).build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("XXX用户系统API")
                .version("1.0")
                .description("Knife4j集成springdoc-openapi示例")
                .termsOfService("http://doc.xiaominfo.com")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://doc.xiaominfo.com")));
    }

}