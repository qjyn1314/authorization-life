# 记忆约定 (Memory Convention)

本文件定义 authorization-life 项目的 Claude Code 记忆方式。所有在本项目工作的 Claude 会话**必须遵守**。

## 核心原则：记忆仅存于项目内

**本项目的全部记忆只存放在项目自身的 `.claude/` 目录，绝不写入用户全局记忆目录。**

- ❌ 不要向 `C:\Users\<user>\.claude\projects\D--devops-code-authorization-life\memory\`（用户全局记忆目录）写入任何记忆文件或 `MEMORY.md` 指针。
- ✅ 所有记忆写入项目目录：`D:\devops_code\authorization-life\.claude\memory\`。
- ✅ 需要记录的新事实，在 `.claude/memory/<分类>/` 下新建主题文件（分类见下文「目录结构」），并在 `.claude/memory/MEMORY.md` 索引中登记。

> 用户全局 `.claude` 目录下的 `projects/<本项目>/memory/` 即使被框架重新创建，也应保持为空；本项目的记忆不存放在那里。

## 记忆读取入口

1. **自动加载**：Claude Code 每次会话自动加载项目根目录的 `CLAUDE.md`，这是记忆主入口，提供快速上下文并指向更深的主题文件。
2. **按需深入**：需要某主题细节时，按分类读取 `.claude/memory/<分类>/` 下对应文件（索引见 `.claude/memory/MEMORY.md`）。

即：`CLAUDE.md`（自动加载）-> `MEMORY.md`（索引）-> 各分类主题文件。

## 目录结构

```
authorization-life/
├── CLAUDE.md                      # 自动加载入口（项目主记忆，精炼）
└── .claude/
    └── memory/
        ├── MEMORY.md              # 记忆索引 + 维护说明
        ├── memory-convention.md   # 本文件：记忆方式约定（系统级）
        ├── architecture/          # 架构与概览
        │   ├── project-overview.md
        │   ├── module-architecture.md
        │   └── tech-stack.md
        ├── domain/                # 业务领域知识
        │   ├── oauth-grant-types.md
        │   └── database-schema.md
        └── engineering/           # 工程实践
            ├── claude-usage-best-practices.md
            ├── coding-conventions.md
            └── local-dev-environment.md
```

## 分类规则

- `architecture/`：项目是什么、如何构建（概览 / 模块 / 技术栈）。
- `domain/`：业务领域知识（授权流程 / 数据模型）。
- `engineering/`：如何在代码库中工作（编码约定 / 本地环境）。
- 根目录：记忆系统自身的索引（`MEMORY.md`）与约定（本文件）。

> 新增内容时按上述规则归入对应子目录；无法明确归类时可在 `MEMORY.md` 讨论。详见 `MEMORY.md` 的「分类约定」。

## 记录什么 / 不记录什么

- **记录**：非显而易见的约定、架构决策、运维事实（端口、版本、启动方式）、设计意图--即代码与 git 历史之外的信息。
- **不记录**：可从代码直接读出的结构、过往已修复的细节、仅与单次对话相关的临时信息。

## 维护

- 事实变化时（新增模块、改端口、升级版本等）同步更新对应文件与 `MEMORY.md` 索引。
- 新增主题文件时放入合适的分类子目录，并在 `MEMORY.md` 对应分类表格登记一行、同步更新「目录结构」树。
- 本约定优先于 Claude Code 默认的用户全局记忆机制；本项目不使用全局记忆。
