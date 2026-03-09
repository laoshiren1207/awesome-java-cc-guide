# Awesome-Java-CC-Guide (Claude Code 学习与实践指南) 🚀

[![Claude Code](https://img.shields.io/badge/Powered%20by-Claude%20Code-purple.svg)](https://claude.ai/code)
[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

本仓库是一个专门为 **Claude Code** (claude.ai/code) 量身定制的 **Java 开发学习指南与实践项目**。

## 🎯 项目目标

本项目的主要目的不是开发特定的业务系统，而是通过一个真实的 Java 项目（Hire-Robot）来展示和学习：
1. **如何高效地与 Claude Code 进行配对编程**。
2. **如何构建 AI 友好的项目结构与编码规范**。
3. **如何利用结构化的工作流实现从需求分析到代码落地的全过程**。

---

## 📚 学习核心 (Core Learning Units)

本项目结构围绕着 AI 协作开发流程展开，建议按以下顺序学习：

### 1. AI 角色与指令集 ([CLAUDE.md](./CLAUDE.md))
了解如何通过 `CLAUDE.md` 为 AI 提供上下文。本项目定义了：
- 项目概述与架构分层规则。
- 常用构建、测试及运行命令。
- 严谨的包命名与类命名约定。

### 2. 编码规范 ([CODING_STANDARDS.md](./CODING_STANDARDS.md))
学习如何制定**利于 AI 理解与生成的代码标准（这里可以替换成自己的编码规范）**：
- 统一的依赖注入方式（构造器注入 + Lombok）。
- 强类型的接口返回包装类 `R<T>`。
- 数据库实体、POJO 与枚举的命名与结构规范。
- 对象转换工具 MapStruct 的标准化使用。

### 3. 高效协作工作流 ([WORKFLOW.md](./WORKFLOW.md)) 🌟
这是本项目的灵魂。学习一套名为 **RP-IF (Research, Plan, Implement, Feedback)** 的协作模型：
- **Research (深度研究)**：利用 `task/research.md` 引导 AI 深入理解已有代码并识别风险。
- **Plan (方案规划)**：在 `task/plan.md` 中进行方案打磨与内联标注（Annotate），在写代码前达成共识。
- **Implement (执行指令)**：使用特定的 Prompt 引导 AI 进行机械化的、高质量的代码实现。
- **Feedback (闭环反馈)**：记录变更与回滚，形成持续改进。

### 4. 故障知识库与防错机制 ([BUG_REP.md](./BUG_REP.md)) 🛡️
这是 AI 的“长期记忆”与“避坑指南”：
- **沉淀经验**：详细记录每一次 Bug 的根因分析与修复路径。
- **防止复现**：在开启修复任务前，引导 AI 先检索历史记录，确保不犯同样的错误。
- **持续迭代**：将新发现的问题与陷阱不断反哺文档，构建项目专属的防错规则集。

---

## 🏗️ 实践案例：Hire-Robot

**Hire-Robot** 是本项目中用于演示上述规范的“教学案例”。它是一个基于 Spring Boot 的招聘自动化后台，具备：
- **OCR 能力** (Tess4J)：处理招聘过程中的图片识别。
- **浏览器自动化** (Playwright)：模拟真实的招聘操作。
- **分层模式**：Controller → Facade (AggService) → Service → Mapper。

---

## 🚀 快速上手

1. **环境准备**：
   - 安装 Java 17, Maven 3.6+, MySQL 8.x。
2. **配置 AI 环境**：
   - 在项目根目录运行 `claude` (Claude Code CLI)。
   - 观察 Claude 如何读取 `CLAUDE.md` 并遵循其职业指导。
3. **尝试一个 Feature**：
   - 按照 [WORKFLOW.md](./WORKFLOW.md) 指引，从 `task/` 复制模板，开启你的第一个 AI 协作任务。

---

## 📁 目录导览

- `/task`: 工作流模板（核心教材）。
- `/feature`: 历史 feature 的研究与规划记录（实战演练记录）。
- `/src`: Hire-Robot 源代码（规范落地的表现形式）。
- `/db`: 数据库初始化脚本。

---

© 2026 Laoshiren协作实践团队。
