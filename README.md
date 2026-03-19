# Awesome-Java-CC-Guide (Java Claude Code 学习与实践指南) 🚀

[![Claude Code](https://img.shields.io/badge/Powered%20by-Claude%20Code-purple.svg)](https://claude.ai/code)
[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

本仓库是一个专门为 **Claude Code** (claude.ai/code) 量身定制的 **Java 开发学习指南与实践项目**。

## 🎯 项目目标

本项目的主要目的不是开发特定的业务系统，而是通过一个真实的 Java 项目来展示和学习：
1. **如何高效地与 Claude Code 进行配对编程**。
2. **如何构建对 Claude Code 友好的项目结构与编码规范**。
3. **如何利用结构化工作流实现从需求分析到代码落地的全过程**。

---

## 📚 学习核心 (Core Learning Units)

本项目结构围绕着 AI 协作开发流程展开，建议按以下顺序学习：

### 1. AI 角色与指令集 ([CLAUDE.md](./CLAUDE.md))
了解如何通过 `CLAUDE.md` 为 AI 提供上下文。本项目定义了：
- 项目概述与架构分层规则。
- 常用构建、测试及运行命令。
- 严谨的包命名与类命名约定。

### 2. 编码规范 ([CODING_STANDARDS.md](./CODING_STANDARDS.md))
学习如何制定**利于 Claude Code 理解与生成的代码标准（这里可以替换成自己的编码规范）**：
- 统一的依赖注入方式（构造器注入 + Lombok）。
- 强类型的接口返回包装类 `R<T>`。
- 数据库实体、POJO 与枚举的命名与结构规范。
- 对象转换工具 MapStruct 的标准化使用。

### 3. 高效协作工作流 ([WORKFLOW.md](./WORKFLOW.md)) 🌟
这是本项目的灵魂。项目要求严格按照 `WORKFLOW.md` 推进任务，采用 **Research → Plan → API（可选）→ Implement → Feedback** 的协作模型，并在关键节点显式使用 Claude Code 能力：
- **Research (深度研究)**：先复制 `task/research.md` 到 `feature/[年]/`，完成代码理解、风险识别与待确认问题整理。
- **AskUserQuestion（确认问题）**：`research.md` 完成后，必须先和用户确认待确认问题；**确认完成前，不得生成 `plan.md`**。
- **Plan (方案规划)**：在 `task/plan.md` 中撰写方案，并通过内联标注（Annotate）反复修订，直到完全认可。
- **API (接口设计与审阅，可选)**：只有涉及 API 变更时才进入 `task/api.md`；如果 API 审阅导致设计调整，需要回到 `plan.md` 继续标注，直至一致。
- **Implement (执行阶段)**：只有 `plan.md` 和 `api.md`（如需要）都批准后，才进入执行阶段；执行时将 Todo 交给 **subagent `plan-executor`** 落地实现。
- **Feedback (闭环反馈)**：用最短的句子记录结果；如果方向错误，直接 `git revert` 后重做计划。

#### 3.1 Subagent 使用说明
为保证“先研究、先确认、再执行”的节奏，本项目将执行职责单独交给 subagent 处理。

- **内置 subagent**：项目在 `.claude/agents/plan-executor.md` 中预置了 `plan-executor`。
- **使用时机**：仅当 `research.md` 已完成确认、`plan.md` 已批准，且 `api.md`（如需要）也已审阅完成后，才能调用。
- **职责边界**：主 Claude 负责研究、提问确认、方案编写与标注；`plan-executor` 负责依据 `plan.md` 的 Todo 逐项执行、更新完成状态并做编译/验证。
- **这样做的原因**：把“方案制定”和“机械执行”拆开，能减少执行偏航，确保实现严格贴合已批准的计划。

### 4. 故障知识库与防错机制 ([BUG_REP.md](./BUG_REP.md)) 🛡️
这是 Claude Code 的“长期记忆”与“避坑指南”：
- **沉淀经验**：详细记录每一次 Bug 的根因分析与修复路径。
- **防止复现**：在开启修复任务前，引导 AI 先检索历史记录，确保不犯同样的错误。
- **持续迭代**：将新发现的问题与陷阱不断反哺文档，构建项目专属的防错规则集。

---

## 🏗️ 实践案例：

**awesome-java-cc-guide** 是本项目中用于演示上述规范的“教学案例”。

---

## 🚀 快速上手

1. **环境准备**：
   - 安装 Java 17, Maven 3.6+, MySQL 8.x。
2. **配置 AI 环境**：
   - 在项目根目录运行 `claude` (Claude Code CLI)。
   - 观察 Claude 如何读取 `CLAUDE.md` 并遵循其职业指导。
3. **尝试一个 Feature**：
   - 按照 [WORKFLOW.md](./WORKFLOW.md) 指引，从 `task/` 复制模板，先完成 `research.md`。
   - 将待确认问题通过 `AskUserQuestion` 与用户确认，通过后再进入 `plan.md`。
   - 如涉及 API 变更，补充并审阅 `api.md`。
   - 最终在执行阶段调用 subagent `plan-executor` 完成 Todo 落地。

---

## 📁 目录导览

- `/task`: 工作流模板（核心教材）。
- `/feature`: 历史 feature 的研究与规划记录（实战演练记录）。
- `/src`: awesome-java-cc-guide 示例源代码（规范落地的表现形式）。
- `/db`: 数据库初始化脚本。

---

© 2026 Laoshiren协作实践团队。
