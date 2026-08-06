# MEMORY.md - authorization-life 记忆索引

本目录是 authorization-life 项目的结构化记忆，按主题**分类**存储于子目录。
Claude Code 每次会话自动加载项目根目录的 `CLAUDE.md`；需要深入某主题时再查阅本目录对应文件。

> ⚠ 本项目记忆**仅存于项目内 `.claude/`**，不写入用户全局记忆目录（`C:\Users\<user>\.claude\projects\...`）。详见 [memory-convention.md](memory-convention.md)。

## 目录结构

```
.claude/memory/
├── MEMORY.md                      # 本文件：索引
├── memory-convention.md           # 记忆方式约定（系统级）
├── architecture/                  # 架构与概览
│   ├── project-overview.md
│   ├── module-architecture.md
│   └── tech-stack.md
├── domain/                        # 业务领域知识
│   ├── oauth-grant-types.md
│   └── database-schema.md
└── engineering/                   # 工程实践
    ├── claude-usage-best-practices.md
    ├── coding-conventions.md
    └── local-dev-environment.md
```

## 记忆文件

### 系统级（根目录）

| 文件 | 说明 |
|------|------|
| [memory-convention.md](memory-convention.md) | 记忆存放位置、读取入口、维护方式（项目内 `.claude` 专用） |

### architecture/ - 架构与概览

| 文件 | 主题 | 说明 |
|------|------|------|
| [project-overview.md](architecture/project-overview.md) | 项目概览 | 项目定位、协议参考、仓库状态、核心设计要点 |
| [module-architecture.md](architecture/module-architecture.md) | 模块架构 | 各 Maven 模块职责、依赖关系与服务端口 |
| [tech-stack.md](architecture/tech-stack.md) | 技术栈 | 后端/前端依赖及精确版本 |

### domain/ - 业务领域知识

| 文件 | 主题 | 说明 |
|------|------|------|
| [oauth-grant-types.md](domain/oauth-grant-types.md) | OAuth 授权模式 | 四种授权方式的流程、接口与存储策略 |
| [database-schema.md](domain/database-schema.md) | 数据库表结构 | 核心表与 Redis/MySQL 存储策略 |

### engineering/ - 工程实践

| 文件 | 主题 | 说明 |
|------|------|------|
| [claude-usage-best-practices.md](engineering/claude-usage-best-practices.md) | Claude Code 使用最佳实践 | 四层记忆模型、黄金规则、协作建议、维护纪律 |
| [coding-conventions.md](engineering/coding-conventions.md) | 编码约定 | 包结构、命名、注释语言、公共能力分布 |
| [local-dev-environment.md](engineering/local-dev-environment.md) | 本地开发环境 | 中间件端口、服务端口、启动步骤、nginx/hosts |

## 如何使用

1. **快速上手**：读 `CLAUDE.md`（自动加载），已含项目全貌与关键事实。
2. **深入某主题**：按上方分类定位，读取对应文件。
3. **记录新事实**：在合适的分类子目录下新建文件，更新本索引与「目录结构」树；不确定分类时先读 [memory-convention.md](memory-convention.md)。

## 分类约定

- `architecture/`：项目是什么、如何构建（概览 / 模块 / 技术栈）。
- `domain/`：业务领域知识（授权流程 / 数据模型）。
- `engineering/`：如何在代码库中工作（Claude 用法 / 编码约定 / 本地环境）。
- 根目录：记忆系统自身的索引与约定。

## 维护说明

- 事实变化时（新增模块、改端口、升级版本等）同步更新对应文件与本索引。
- 记忆应记录**非显而易见、代码与 git 历史之外**的约定与决策；可从代码直接读出的结构无需赘述。
- 新增分类或文件后，更新「目录结构」树与对应表格。
