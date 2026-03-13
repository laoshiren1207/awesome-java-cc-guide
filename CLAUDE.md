# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

本文档为 Claude Code (claude.ai/code) 在本项目中工作时提供指导。

## 项目概述

**本项目** 是一个专门为 **Claude Code** 量身定制的 **Java 开发学习指南与实践项目**，基于 Spring Boot 3.1.1（Java 17）开发，集成了 OCR（Tess4J）和浏览器自动化（Playwright）能力。

项目的主要目标是展示如何：
1. 高效地与 Claude Code 进行配对编程
2. 构建 AI 友好的项目结构与编码规范
3. 利用结构化的工作流实现从需求分析到代码落地的全过程

**注意**：本项目不是生产级系统，而是教学和实践项目。

## 核心学习内容

### 1. AI 协作工作流 (RP-IF 模型)
项目采用 **RP-IF (Research, Plan, Implement, Feedback)** 协作模型：
- **Research (深度研究)**: 使用 `task/research.md` 模板引导 AI 深入理解已有代码并识别风险
- **Plan (方案规划)**: 在 `task/plan.md` 中进行方案打磨与内联标注，达成共识后再执行
- **Implement (执行指令)**: 使用特定 Prompt 引导 AI 进行机械化、高质量的代码实现
- **Feedback (闭环反馈)**: 记录变更与回滚，形成持续改进

### 2. 防错机制
项目维护 **BUG 知识库** (`BUG_REP.md`)，包含：
- 每一次 Bug 的根因分析与修复路径
- 防止复现的规则与陷阱
- 持续迭代的防错机制

## 工作流程

项目工作开展时**必须**按照 `./WORKFLOW.md` 进行项目的开发工作。

关键流程：
1. 新建任务时：复制 `task/research.md`、`task/plan.md` 和 `task/api.md` 到 `feature/[年]/` 目录并重命名
2. 思考阶段：先完成 `research.md`，验证 Claude 对代码库的理解
3. 规划阶段：在 `plan.md` 中写方案，反复标注直至完全认可
4. **API 文档阶段决策**：
   - 如果本次开发涉及API变动，则进入API文档编写阶段
   - 如果本次开发不涉及API变动，则直接跳过API文档阶段，进入执行阶段
5. **API 文档编写审阅阶段**：
   - 拷贝 `task/api.md` 并填充此次任务的API，编写完成进入审阅阶段
   - 在 api.md 中修改部分API设计直到完美
   - **重要提示**：API 文档的变动可能会导致计划（plan.md）部分代码的调整，如有修改需重新标注直至完美
   - 审阅完成前，批准前绝不执行后续操作
6. **执行阶段**：只有在API文档（如涉及）和计划（plan.md）都完美后，才开始执行阶段。复制执行指令给 Claude 机械完成所有 Todo
7. 反馈阶段：用最短的句子反馈，方向错误直接 `git revert` 重写计划

## 项目目录结构

```
awesome-java-cc-guide/
├── task/              # 工作流模板（核心教材）
│   ├── research.md    # 深度研究模板
│   ├── api.md         # api 模板
│   └── plan.md        # 方案规划模板
├── feature/           # 历史 feature 的研究与规划记录（实战演练记录）
│   └── [年]/          # 按年份分组的 feature 记录
├── src/               # 源代码（规范落地的表现形式）
│   ├── main/java/com/example/  # 主源码包
│   └── test/java/com/example/  # 测试代码
├── db/                # 数据库初始化脚本
├── README.md          # 项目说明文档
├── CLAUDE.md          # AI 协作指导文档（本文档）
├── CODING_STANDARDS.md # 编码规范
├── WORKFLOW.md        # 工作流程规范
└── BUG_REP.md         # BUG 知识库
```

## 常用命令

```bash
# 构建项目
mvn clean package

# 运行应用
mvn spring-boot:run

# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=ClassName

# 运行单个测试方法
mvn test -Dtest=ClassName#methodName

# 编译项目（不运行测试）
mvn compile

# 清理编译输出
mvn clean

# 查看项目依赖树
mvn dependency:tree
```

## 本地开发环境搭建

1. 复制配置模板：`cp src/main/resources/application-dev-sample.yaml src/main/resources/application-dev.yaml`
2. 填写 `application-dev.yaml` 中的数据库连接
3. 执行 `db/[年]/[年月日].sql` 下对应的 SQL 脚本建表（如 `db/2026/20260305.sql`）

## 架构概览

分层架构，层间严格单向依赖：

```
Controller → Facade(AggService) → Service → Mapper (DB)
                    ↓
                Vendor（第三方集成）
```

**层间访问规则（必须遵守）：**
- `service`：只能调用**本实体**的 Mapper，禁止跨实体调用
- `facade`：编排多个 Service 和 Vendor，禁止直接调用 Mapper
- `controller`：只调用 Facade，返回 `R<T>` 包装结果

## 命名约定

新增模块时，按领域（domain）在包内建子目录，不要平铺：

| 层 | 接口命名 | 实现类命名 | 子目录示例 |
|---|---|---|---|
| Controller | — | `XxxController` | `controller/candidate/` |
| Facade | `IXxxAggService` | `XxxAggServiceImpl` | `facade/candidate/` |
| Service | `IXxxService` | `XxxServiceImpl` | `service/impl/` |
| Mapper | `XxxMapper` | — | `mapper/` |
| POJO | `XxxParam` / `XxxVO` / `XxxDTO` | — | `pojo/candidate/` |
| Enum | `XxxEnum implements BaseDbEnum` | — | `enums/candidate/` |
| Converter | `XxxConverter` | — | `converter/` |

## 包结构

```
com.example
├── controller/{domain}/  # 接口层，返回 R<T>
├── facade/{domain}/      # 业务编排层（IXxxAggService + XxxAggServiceImpl）
├── service/              # 单实体业务接口（IXxxService）
│   └── impl/             # 业务实现（XxxServiceImpl）
├── mapper/               # MyBatis Plus Mapper（@MapperScan 扫描，XML 在 resources/mapper/*.xml）
├── entity/               # 数据库实体（均继承 BaseEntity）
├── pojo/{domain}/        # DTO / VO / Param，按领域分子目录
├── enums/{domain}/       # 业务枚举（均实现 BaseDbEnum），按领域分子目录
├── vendor/               # 第三方系统集成适配器
├── converter/            # MapStruct 转换器（componentModel = "spring"）
├── configure/            # 配置类
│   ├── advice/           # 全局异常处理（ExceptionControllerAdvice）
│   ├── mybatis/          # MybatisPlusConfig（分页插件）、MetaObjectHandlerConfig（自动填充）
│   ├── thread/           # 业务线程池（MDC 传播）
│   └── web/              # 请求 ID 注入、响应体增强
├── common/exception      # BusinessException / ValidException / BusinessStatus
└── utils/                # ValidUtil / JsonUtil / OkHttpUtil / LRUMapUtil
```

## 关键技术细节

### 注解处理器链（pom.xml maven-compiler-plugin）
Lombok → MapStruct → lombok-mapstruct-binding 三者顺序不可变，否则 MapStruct 无法识别 Lombok 生成的 getter/setter。

### BaseEntity 自动填充
所有实体继承 `BaseEntity`，自动提供 `id`（自增）、`createTime`/`updateTime`（MetaObjectHandlerConfig 填充）、`isDeleted`（逻辑删除）。

### 线程池
`baseTaskExecutor` bean（`BizThreadPoolConfig`）使用 `BizThreadPoolExecutor` + `MDCRunnable` 以保证 MDC（requestId）在异步任务中传播。

### 请求追踪
`RequestIdFilter` 为每个请求生成唯一 `requestId`，存入 MDC；`ResBodyRequestIdAdvice` 将其注入响应体的 `R.requestId` 字段。

### 异常处理
业务异常抛 `BusinessException`，校验异常抛 `ValidException`（通过 `ValidUtil.validate(param)`），均由 `ExceptionControllerAdvice` 统一拦截返回 `R<T>`。

## 配置说明

- `application.yaml`：公共配置，默认激活 `dev` profile，Jackson 时区 GMT+8
- `application-dev-sample.yaml`：本地开发配置模板，复制为 `application-dev.yaml` 并填写真实值（数据库连接等）
- MyBatis Plus mapper XML 位置：`classpath*:mapper/*.xml`
- p6spy 已集成，可在 `application.yaml` 中取消 p6spy 日志注释开启 SQL 日志

## 编码规范

项目开发时**必须**按照 `./CODING_STANDARDS.md` 进行开发。

## BUG修复

修复BUG前先**查阅** `./BUG_REP.md`。修复后，BUG**必须**记录在 `./BUG_REP.md` 文件中。
