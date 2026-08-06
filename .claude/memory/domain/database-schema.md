# 数据库表结构

数据库：`life`（MySQL 9.3.0，端口 33010）。初始化脚本：`db/life_20250621_allinit.sql`。

## 核心表

| 表名 | 说明 |
|------|------|
| `liam_oauth_client` | OAuth 授权客户端（对应官方 registered-client-schema） |
| `liam_user` | 用户表 |
| `liam_user_group` | 用户分组表 |
| `lsys_lov` | 字典主表 |
| `lsys_lov_value` | 字典子表（字典值） |
| `lsys_temp` | 模板主表 |
| `lsys_temp_user` | 模板与用户关联表 |

## 权限相关表（见 `design_doc/用户功能权限设计文档.md`）

| 表名 | 说明 |
|------|------|
| `liam_role` | 角色表（`parent_role_code` 树形、`tenant_id` 租户隔离） |
| `liam_menu` | 菜单/资源表（`menu_type`：menu / button / api） |
| `liam_role_menu` | 角色-菜单关联表 |

权限模型参考 Linux 用户/文件权限：

- 资源类型：`button`（按钮）/ `api`（接口）/ `menu`（菜单）
- 权限标志：`show_flag`（菜单/按钮是否显示）、`access_flag`（接口是否可请求）

## 存储策略：Redis vs MySQL

spring-authorization-server 默认的 JDBC 存储在本项目改为 Redis：

| 官方 schema | 本项目存储 | 实现类 |
|-------------|-----------|--------|
| oauth2-authorization（token / code / refresh） | Redis | `life-authserver/.../infra/security/service/RedisOAuth2AuthorizationService.java` |
| oauth2-authorization-consent | Redis | `life-authserver/.../infra/security/service/RedisOAuth2AuthorizationConsentService.java` |
| oauth2-registered-client | MySQL `liam_oauth_client` | — |

## 通用字段约定（权限相关表）

普遍包含：`tenant_id`（租户隔离）、`version_num`（版本号）、`created_by_user` / `created_by_emp`、`updated_by_user` / `updated_by_emp`、`created_time` / `updated_time`。
