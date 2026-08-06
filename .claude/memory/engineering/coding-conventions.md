# 编码约定

## 语言与注释

- **注释、文档、commit message、错误信息均使用中文。**
- 错误信息走 i18n：各模块 `src/main/resources/messages*_zh_CN.properties` / `*_en_US.properties`
  （life-core、life-utils、life-authserver、life-system 各有自己的 message 文件）。

## 包结构

Java 包根 `com.authorization.life`，但部分模块有差异：

| 模块 | 包 |
|------|----|
| life-authserver | `com.authorization.life.auth.*`（如 `infra.security.service`） |
| life-gateway | `com.authorization.gateway.*` |
| life-utils | `com.authorization.utils.*` |
| life-core | `com.authorization.life.core.*` |
| life-system | `com.authorization.life.system.*`（按命名惯例，使用前请确认） |

资源分隔符：`@`（`resource.delimiter=@`，Maven 资源过滤用 `@xxx@` 而非 `${xxx}`）。

## 持久层

- MyBatis-Plus；Mapper XML 路径：`classpath*:/mapper/*Mapper.xml`（各服务 `resources/mapper/`）。
- SQL 打印：`mybatis-plus.configuration.log-impl=StdOutImpl`（开发期开启）。
- 分页：PageHelper（`helper-dialect=mysql`，`reasonable=true`，`support-methods-arguments=true`）。
- 连接池：Druid（`druid-spring-boot-3-starter`），filters `stat,wall,slf4j`。

## 公共能力分布

| 能力 | 位置 |
|------|------|
| 当前登录用户获取 / JWT 解析 / 加解密 / 事件监听 | life-utils |
| 公共认证配置 / JWT 解析 / 服务上下线 | life-core |
| OAuth2.0 授权认证 | security-start-life |
| 分布式锁 / Redis 发布订阅 | redis-start-life |
| 字典翻译 / 查询 | lov-start-life |
| 请求参数校验 | valid-start-life |
| 邮件发送 | mail-start-life |
| Dubbo + Nacos | dubbo-start-life |

## 配置与工具

- 配置加密：Jasypt（`jasypt-spring-boot-starter`）。
- bean 转换：MapStruct（`mapstruct` + `mapstruct-processor`）。
- JSON：Fastjson2 + life-utils 中自定义 ObjectMapper 适配。
- 线程上下文：Transmittable-Thread-Local（跨线程池传递登录上下文）。
- 工具类：Hutool（`hutool-all`）、Guava、commons-lang3。

## 版本管理

- 所有内部模块版本统一用 `${revision}=1.0.0`（根 pom `<revision>`），通过 `dependencyManagement` 管理内部依赖。
- nimbus-jwt 版本须与 spring-authorization-server 内置版本保持一致（当前 9.47）。

## 日志

- 各服务自带 `logback-spring.xml`。
- 开发期 authserver：`logging.level.web=trace`、`logging.level.org.springframework.security=trace`。
- 网关：`logging.level.web=debug`、`com.authorization.gateway=debug`。
- Nacos ClientWorker 噪声日志统一降为 `warn`。
