package com.authorization.life.system.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * System服务的文档配置类
 *
 * @author wangjunming
 * @date 2022/12/20 16:49
 */
@Configuration
public class Knife4jSystemConfig {

    private static final String PATHS_TO_MATCH = "/**";

    private static final String[] PACKAGED_TO_MATCH = {"com.authorization.life.system.api.controller"};

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("系统配置模块")
                .pathsToMatch(PATHS_TO_MATCH)
                .packagesToScan(PACKAGED_TO_MATCH).build();
    }

    /**
     * 本工程 访问 /doc.html, 主页显示的内容说明
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI systemOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("系统配置API")
                .description("关于系统的字典数据,全局样式的配置.")
                .contact(new Contact()
                        .name("wangjunming"))
                .version("1.0")
                .termsOfService("https://doc.authorization.life/system-life")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://doc.authorization.life")));
    }

}