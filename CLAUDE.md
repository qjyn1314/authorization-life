# CLAUDE.md - authorization-life 项目记忆

> 本文件由 Claude Code 每次会话自动加载，提供项目快速上下文。深入某主题时查阅 `.claude/memory/`（索引见 `.claude/memory/MEMORY.md`，按 architecture / domain / engineering 分类）。

## ⚠ 记忆约定（必读）

本项目的记忆**只存放在项目自身 `.claude/` 目录，不写入用户全局记忆目录**--不要向 `C:\Users\<user>\.claude\projects\D--devops-code-authorization-life\memory\` 写任何记忆文件或索引。

- 需记录新事实时：在 `.claude/memory/<分类>/` 下新建/更新主题文件，并在 `.claude/memory/MEMORY.md` 索引登记。
- 记忆入口：本文件（自动加载）-> `.claude/memory/MEMORY.md`（索引）-> 各分类主题文件。
- 完整约定见 `.claude/memory/memory-convention.md`；Claude Code 使用最佳实践见 `.claude/memory/engineering/claude-usage-best-practices.md`。

## 项目简介

authorization-life 是一个 **OAuth 2.0 授权服务** 工程，前后端分离实践：自定义 Maven 多模块工程与 spring-security + spring-authorization-server 深度整合。

- 后端：Spring Cloud 微服务（授权服务 + 网关 + 系统管理）
- 前端：Vue3 函数式编程（`front-auth`）
- 当前开发分支：`master-feauther-v7.0.0`；主分支：`master`
- 注释、文档、commit message 均使用**中文**

## 技术栈（关键版本）

- JDK 17 · Maven 多模块（`groupId: com.authorization.life`，`revision=1.0.0`）
- Spring Boot 3.5.0 · Spring Cloud 2025.0.0 · Spring Cloud Alibaba 2023.0.3.3 · Dubbo 3.3.4
- Spring Authorization Server（OAuth2.0）· nimbus-jwt 9.47
- MyBatis-Plus 3.5.12 · PageHelper · Druid 1.2.25 · MySQL 9.3.0
- Nacos v2.5.1（注册中心；配置中心关闭）· Redis 8 · Knife4j 4.5.0
- 前端：Vue 3.5.16 · Pinia 3.0.3 · Element Plus 2.10.0 · Axios 1.9.0 · @vue/cli 5.0.8 · Node v24.1.0

详见 `.claude/memory/architecture/tech-stack.md`。

## 模块结构

| 模块 | 职责 |
|------|------|
| `life-base` | 公共依赖（life-common / life-core / life-utils） |
| `life-start-parent` | 各 starter（dubbo / knife4j / lov / mail / mybatis / redis / security / valid-start-life） |
| `life-remote-api` | 服务间 Dubbo 调用接口（authserver-remote-api / system-remote-api） |
| `life-gateway` | 网关：动态路由、鉴权、URL 校验、断路器 |
| `life-authserver` | 授权认证服务端（OAuth2.0 server），颁发 token |
| `life-system` | 系统管理服务（字典、模板等，OAuth2.0 client） |
| `front-auth` | Vue3 前端工程 |

详见 `.claude/memory/architecture/module-architecture.md`。

## 本地运行

**中间件端口：** MySQL `33010`（库 `life`，root/123456）· Nacos `12095` · Redis `12040`（密码 123456，db 0）· 前端 dev `18888` · nginx `80`（域名 `dev.authorization.life`）

**服务端口：** gateway `9000` · authserver `9030` · system `9050`

**启动顺序：**
1. 初始化数据库：执行 `db/life_20250621_allinit.sql`
2. 启动 nacos / mysql / redis / nginx，并配置 hosts `127.0.0.1 dev.authorization.life`
3. 后端（JDK17）：启动 `AuthServerLifeApplication`(9030) 与 `GatewayLifeApplication`(9000)，按需启 system(9050)
4. 前端：`cd front-auth && npm install && npm run dev`
5. 访问：http://dev.authorization.life/clients

**构建：** 根目录 `mvn clean install -DskipTests`（JDK17）

详见 `.claude/memory/engineering/local-dev-environment.md`。

## OAuth2.0 授权模式

四种授权方式，token 接口均为 `POST /dev-api/auth-life/oauth2/token`（经网关）：

- 授权码模式 `authorization-code`（前端临时授权页 `front-auth/src/views/auth/TemporaryAuth.vue`）
- 客户端凭证 `client_credentials`
- 用户名密码 `password`
- 验证码模式 `captcha_code`（自定义，先 `GET /oauth/send-login-code` 取 uuid）

**token / 临时 code / refresh token / 授权同意(consent) 均存 Redis，不落库。** 详见 `.claude/memory/domain/oauth-grant-types.md`。

**默认测试账号：** `qjyn1314@163.com` / `admin`；默认 client `passport`，secret `3MMoCFo4nTNjRtGZ`。

## 编码约定

- Java 包根：`com.authorization.life`（gateway 为 `com.authorization.gateway`，utils 为 `com.authorization.utils`）
- 错误信息走 i18n：各模块 `resources/messages*_zh_CN.properties` / `*_en_US.properties`
- 配置加密 Jasypt；bean 转换 MapStruct；JSON Fastjson2 + 自定义 ObjectMapper；线程上下文 Transmittable-Thread-Local
- MyBatis-Plus，Mapper XML 在 `resources/mapper/*Mapper.xml`

详见 `.claude/memory/engineering/coding-conventions.md`。

## 重要文件位置

- 总文档：`README.md`（含完整 nginx.conf 与各模式请求示例）
- 设计文档：`design_doc/用户登录及注册设计文档.md`、`design_doc/用户功能权限设计文档.md`
- 建表 SQL：`db/life_20250621_allinit.sql`
- 数据库表说明：`.claude/memory/domain/database-schema.md`
