# 模块架构

根 `pom.xml` 聚合模块：`life-start-parent`、`life-base`、`life-remote-api`、`life-gateway`、`life-authserver`、`life-system`。

## life-base（公共依赖）

| 子模块 | 职责 |
|--------|------|
| `life-common` | web 服务、注册组件、工具类的公共依赖；需与前端交互的 web 服务引入 |
| `life-core` | 公共认证配置、JWT 解析、服务上下线注册、服务启停配置 |
| `life-utils` | 当前登录用户获取、JWT 解析、ObjectMapper 适配、加解密工具、Spring Boot 事件监听 |

## life-start-parent（starter 集合）

| starter | 职责 |
|---------|------|
| `dubbo-start-life` | Dubbo 配置 + Nacos 注册/配置中心 |
| `knife4j-start-life` | OpenApi 文档 |
| `lov-start-life` | 字典翻译、查询字典 |
| `mail-start-life` | 邮件发送 |
| `mybatis-start-life` | MyBatis-Plus 持久层 |
| `redis-start-life` | Redis 工具、发布订阅、分布式锁 |
| `security-start-life` | OAuth2.0 授权认证 |
| `valid-start-life` | 请求参数校验 |

## life-remote-api（服务间调用接口）

> 前提：已依赖 `dubbo-start-life`。provider/consumer 配置在各 `*-remote-api` 的 `resources/remote/*.xml`。

| 子模块 | 职责 |
|--------|------|
| `authserver-remote-api` | authserver 提供的 Dubbo 服务接口 |
| `system-remote-api` | system 提供的 Dubbo 服务接口 |

## 业务服务

| 模块 | 服务名 | 端口 | 角色 |
|------|--------|------|------|
| `life-gateway` | `gway-life` | 9000 | 网关：动态路由（服务上下线监听）、Authorization 解析与权限校验、自定义路由、URL 校验、断路器 |
| `life-authserver` | `auth-life` | 9030 | 授权认证服务端（OAuth2.0 server）：认证用户、颁发 token |
| `life-system` | `system-life` | 9050 | 系统管理服务（OAuth2.0 client）：字典、业务配置、模板等 |

启动类（README 明确）：`life-authserver/.../AuthServerLifeApplication.java`、`life-gateway/.../GatewayLifeApplication.java`。

## 前端

`front-auth`：Vue3 工程，包含认证授权中心（登录/授权）、系统字典管理、消息模板、表单模板管理等。临时授权页 `front-auth/src/views/auth/TemporaryAuth.vue` 负责用 code 换 token 并存入 cookie/localStorage。

## 依赖关系要点

- 业务服务（authserver / system / gateway）依赖 `life-base` 子模块 + 所需 `life-start-parent` starter。
- 服务间 Dubbo 调用通过 `life-remote-api` 的接口包。
- 所有服务统一注册到 Nacos（namespace `public`，group `APPLICATION`），Redis 统一用 0 号库。
- 内部模块版本统一 `${revision}=1.0.0`，由根 pom `dependencyManagement` 管理。
