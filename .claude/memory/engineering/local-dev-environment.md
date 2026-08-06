# 本地开发环境

## 中间件端口

| 中间件 | 端口 | 备注 |
|--------|------|------|
| MySQL | 33010 | 库名 `life`，root / 123456 |
| Nacos | 12095 | v2.5.1，namespace `public`，group `APPLICATION` |
| Redis | 12040 | 密码 123456，database 0（所有服务统一） |
| nginx | 80 | 域名 `dev.authorization.life` |
| 前端 dev server | 18888 | vue-cli-service serve |

## 服务端口

| 服务 | 服务名 | 端口 |
|------|--------|------|
| 网关 | gway-life | 9000 |
| 授权服务 | auth-life | 9030 |
| 系统服务 | system-life | 9050 |

## 启动步骤

1. `git clone https://github.com/qjyn1314/authorization-life.git`
2. 初始化数据库：执行 `db/life_20250621_allinit.sql`
3. 启动 MySQL(33010)、Nacos(12095)、Redis(12040)、nginx(80)
4. 配置 hosts：`127.0.0.1 dev.authorization.life`
5. 后端（JDK17）：先启 `AuthServerLifeApplication`(9030) 与 `GatewayLifeApplication`(9000)，按需启 system(9050)
6. 前端：`cd front-auth && npm install && npm run dev`
7. 访问：http://dev.authorization.life/clients

## 构建

根目录：`mvn clean install -DskipTests`（需 JDK17）

## nginx 关键配置（完整见 README.md）

- `dev.authorization.life` 监听 80
- `location /dev-api/` → `proxy_pass http://127.0.0.1:9000/`（后端网关）
- `location /` → `proxy_pass http://127.0.0.1:18888`（前端 Vue）
- 需配置 `server_names_hash_max_size/bucket_size=2048`（域名长度相关报错）
- 需配置 `map $http_upgrade $connection_upgrade`（WebSocket / sockjs-node）

## 监控

- authserver Druid 监控：`/druid/`，账号 admin / admin，仅允许 127.0.0.1
- Knife4j 文档：网关聚合（`knife4j.gateway.enable=true`），各服务 `knife4j.enable=true`

## 前端多环境

`.env.development/.env.test/.env.pre/.env.production`

- 启动：`npm run dev|test|pre|prod`
- 构建：`npm run build:dev|test|pre|prod`
- lint：`npm run lint`
