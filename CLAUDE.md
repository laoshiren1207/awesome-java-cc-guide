# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

本文档为 Claude Code (claude.ai/code) 在本项目中工作时提供指导。

## 项目概述

**hire-robot** 是一个基于 Spring Boot 3.1.1（Java 17）开发的招聘自动化后台应用，集成了 OCR（Tess4J）和浏览器自动化（Playwright）能力。

## 工作流程

项目工作开展时**必须**按照 `./WORKFLOW.md` 进行项目的开发工作。

关键流程：新建 feature 时，将 `task/research.md` 和 `task/plan.md` 模板复制到 `feature/[年]/` 目录下并重命名。先完成深度研究（research），再规划（plan），标注通过后才执行。

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
