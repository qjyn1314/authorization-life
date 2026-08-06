# 技术栈

## 后端

基础（根 `pom.xml`，`groupId: com.authorization.life`，`revision=1.0.0`）：

| 依赖 | 版本 | 说明 |
|------|------|------|
| JDK | 17 | 编译目标 |
| Spring Boot | 3.5.0 | |
| Spring Cloud | 2025.0.0 | |
| Spring Cloud Alibaba | 2023.0.3.3 | |
| Dubbo | 3.3.4 | 服务间调用 |
| Spring Authorization Server | 随 Boot 管理 | OAuth2.0 server |
| nimbus-jose-jwt | 9.47 | JWT，须与 authorization-server 内置版本一致 |
| MyBatis-Plus | 3.5.12 | ORM（含 mybatis-plus-jsqlparser） |
| PageHelper | 2.1.0 | 分页 |
| Druid | 1.2.25 | 连接池（druid-spring-boot-3-starter） |
| MySQL Connector/J | 9.3.0 | |
| Knife4j | 4.5.0 | OpenApi 文档 |
| Hutool | 5.8.29 | 工具类（hutool-all） |
| Fastjson2 | 2.0.57 | JSON |
| Guava | 33.2.1-jre | |
| MapStruct | 1.5.5.Final | bean 转换 |
| Jasypt | 3.0.5 | 配置加解密（jasypt-spring-boot-starter） |
| Transmittable-Thread-Local | 2.14.5 | 线程上下文跨池传递 |
| slf4j-api | 2.0.13 | 日志门面 |

注册/配置中心：**Nacos v2.5.1**（服务发现开启；配置中心 `config.enabled=false`，配置走本地 `bootstrap.yml` / `application.properties`）。group `APPLICATION`，namespace `public`。

## 前端（front-auth）

| 依赖 | 版本 |
|------|------|
| Vue | ^3.5.16 |
| Pinia | ^3.0.3（+ pinia-plugin-persistedstate ^4.3.0） |
| Element Plus | ^2.10.0（+ @element-plus/icons-vue ^2.3.1） |
| Axios | ^1.9.0 |
| Vue Router | ^4.5.1 |
| @vue/cli | ~5.0.8 |
| Node / npm | v24.1.0 / 11.4.0 |
| 其他 | echarts ^5.6.0、@vueuse/core ^13.3.0、js-cookie ^3.0.5、mitt ^3.0.1、nprogress ^0.2.0、pubsub-js ^1.9.5、qs ^6.14.0、uuid ^11.1.0、vuex ^4.1.0 |

构建工具：vue-cli-service；多环境配置：`.env.development/.env.test/.env.pre/.env.production`。

## 中间件版本

MySQL 9.3.0 · Nacos v2.5.1 · Redis 8.0.1 · nginx 1.27.2
