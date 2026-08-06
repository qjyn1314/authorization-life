# OAuth2.0 授权模式

token 统一接口（经网关）：`POST http://dev.authorization.life/dev-api/auth-life/oauth2/token`

## 1. 授权码模式（authorization-code）

完整链路：

1. 前端默认页 `http://dev.authorization.life/clients`，点击「请求授权URL」请求：
   `GET /dev-api/auth-life/oauth2/authorize?response_type=code&client_id=passport_dev&scope=TENANT&state=xxx&redirect_uri=http://dev.authorization.life/auth-redirect`
2. 未登录则跳转登录页 `/login?redirect_uri=...`，输入用户名密码。
3. 登录通过后重定向到 `/auth-redirect?code=xxx&state=xxx`（前端临时授权页 `TemporaryAuth.vue`）。
4. 临时页可再次校验（短信/邮箱验证码、二维码），通过后用 code 请求 `/oauth2/token` 换取自定义 JWT accessToken。
5. token 存 cookie / localStorage，跳转系统首页。

> 临时 code 授权页存在意义：登录成功但颁发 token 前，可做二次身份验证。

## 2. 客户端凭证（client_credentials）

`POST /oauth2/token`，参数：`grant_type=client_credentials`、`client_id=passport`、`client_secret=3MMoCFo4nTNjRtGZ`

## 3. 用户名密码模式（password）

`POST /oauth2/token`，参数：`grant_type=password`、`client_id=passport`、`client_secret=3MMoCFo4nTNjRtGZ`、`username=qjyn1314@163.com`、`password=admin`

## 4. 验证码模式（captcha_code，自定义扩展）

1. 发送验证码：`GET /dev-api/auth-life/oauth/send-login-code?username={手机号或邮箱}`，返回验证码 uuid。
2. 用验证码换 token：`POST /oauth2/token`，参数：
   - `grant_type=captcha_code`
   - `client_id=passport`、`client_secret=3MMoCFo4nTNjRtGZ`
   - `username={手机号或邮箱}`
   - `captcha_uuid={返回的 uuid}`（对应 Redis key）
   - `captcha_code={6位验证码}`

## 默认测试账号 / client

- 用户名：`qjyn1314@163.com`，密码：`admin`
- client：`passport`（密码模式等）/ `passport_dev`（授权码模式），client_secret：`3MMoCFo4nTNjRtGZ`

## 存储策略

| 数据 | 存储 | 实现类 |
|------|------|--------|
| AccessToken / 临时 code / RefreshToken | Redis | `life-authserver/.../infra/security/service/RedisOAuth2AuthorizationService.java` |
| 授权同意(consent) | Redis | `life-authserver/.../infra/security/service/RedisOAuth2AuthorizationConsentService.java` |
| OAuth 客户端 | MySQL `liam_oauth_client` | 对应官方 registered-client-schema |

## 其它登录注册相关接口

（见 `design_doc/用户登录及注册设计文档.md`）

- `POST /oauth/client`：按域名获取 client 信息（client_id / client_secret）
- `POST /oauth/send-email-code-register` / `POST /oauth/email-register`：邮箱注册（验证码有效期 10 分钟）
- `POST /oauth/send-email-code-reset-pwd` / `POST /oauth/reset-password`：重置密码
- `GET /oauth/self-user`：解析 accessToken 获取当前用户
- `GET /oauth/valid-picture-code`：密码错误 3 次后的图形验证码
- `GET /oauth/send-login-code`：发送登录验证码
- `POST /v1/user/user-approved` / `user-audit-reject` / `user-unlock`：用户审核与解锁（更新 activedFlag / lockedFlag）
