# 项目概览

## 定位

authorization-life 是一个 **OAuth 2.0 授权服务** 教学与实践工程，目标是前后端分离地落地 OAuth2.0：

- 自定义 Maven 多模块工程，与 spring-security + spring-authorization-server 深度整合。
- 既实现标准授权模式，也扩展自定义模式（如验证码模式 `captcha_code`）。
- 配套 Vue3 前端，演示完整登录授权链路。

## 协议参考

- OAuth 2.0：https://datatracker.ietf.org/doc/html/rfc6749
- OAuth 2.1：https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-13
- 授权模式图解：https://learnku.com/articles/20082
- 对应博客专栏：https://blog.csdn.net/qq_38046739/category_12090398.html

## 仓库状态

- 主分支：`master`
- 当前开发分支：`master-feauther-v7.0.0`（近期提交集中在验证码模式与用户名密码模式的完善）
- Git 用户：qjyn1314
- GitHub：https://github.com/qjyn1314/authorization-life.git

## 核心设计要点

- **token / 临时 code / refresh token / 授权同意(consent)** 均存 Redis，不落库（覆盖 spring-authorization-server 默认的 JDBC 实现）。
- 授权客户端表对应 `liam_oauth_client`（参考官方 oauth2-registered-client-schema）。
- 多租户权限设计参考 Linux 用户/文件权限模型，租户层级：租户 → 组织/法律实体 → 部门 → 岗位 → 员工。
- 每个租户即一个顶级角色（管理员），该租户新员工的权限继承自该管理员。
