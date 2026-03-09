# 编码规范 (Coding Standards)

本文档定义了项目的编码规范和最佳实践，旨在提高代码质量、可读性和可维护性。

## 1. 技术栈基础

- **JDK版本**: Java 17 (LTS)
- **核心框架**: Spring Boot 3.1.1
- **持久层**: MyBatis Plus 3.5.10.1
- **数据库**: MySQL 8.x
- **HTTP客户端**: OkHttp 5.x
- **对象映射**: MapStruct 1.5.5.Final
- **代码简化**: Project Lombok 1.18.28
- **验证框架**: Jakarta Validation (Hibernate Validator)
- **测试框架**: JUnit 5, Mockito

## 2. 命名规范

### 2.1 包命名
- 使用小写字母，单词间不加分隔符。
- 基础包名: `com.xxx.xxxx`
- 分层建议:
  - `controller`: 接口层
  - `service`: 业务逻辑接口层，单个实体的业务逻辑 只允许调用本实体的持久化层，不允许跨实体调用
  - `service.impl`: 业务逻辑实现层
  - `facade`: 业务编排/聚合层，多业务逻辑的编排，不允许直接调用mapper 持久化层
  - `mapper`: 持久化操作层
  - `entity`: 数据库实体模型
  - `pojo`: 数据传输对象 (DTO/VO/Param)
  - `vendor`: 第三方系统集成适配器
  - `config`: 配置类
  - `utils`: 通用工具类
  - `enums`: 业务枚举类

### 2.2 类与接口命名
- **接口 (Interface)**: 必须以 `I` 开头。例如: `ICallRobotAggService`。
- **实现类 (Implementation)**: 必须以 `Impl` 结尾。例如: `CallRobotAggServiceImpl`。
- **Controller**: 以 `Controller` 结尾。例如: `BaiduRobotController`。
- **Mapper**: 以 `Mapper` 结尾。例如: `RobotMapper`。
- **POJO 模型**:
  - `Param`: 入参对象，用于 Controller 接收参数。例如: `CreateEditRobotParam`。
  - `VO`: 视图对象，用于向前端/调用方返回数据。例如: `RobotVO`。
  - `DTO`: 内部传输对象。
  - `Entity`: 数据库对应实体，放在 `entity` 包下。

### 2.3 方法命名
- **获取单条数据**: `getByKey` / `getById`
- **获取列表**: `listByXXX`
- **分页查询**: `pageByXXX`
- **新增**: `save` / `add`
- **修改**: `update` / `edit`
- **删除**: `remove` / `delete`
- **统计**: `countByXXX`

## 3. 代码风格

### 3.1 依赖注入
- **推荐**: 使用构造器注入。
- **实践**: 结合 Lombok 的 `@AllArgsConstructor` 或 `@RequiredArgsConstructor`。
- **避免**: 在字段上直接使用 `@Autowired`。

```java
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper; // 使用 final 确保不可变性
}
```

### 3.2 Lombok 使用
- 实体类、POJO 推荐使用 `@Data` 或 `@Getter`/`@Setter`。
- 推荐使用 `@Builder` 进行对象构建。
- 日志记录使用 `@Slf4j`。

### 3.3 异常处理
- 接口统一返回 `R<T>` 对象，禁止使用 `R<?>` 通配符泛型。
- 成功：`R.ok(data)`，失败抛业务异常由全局 handler 处理。
- `R` 内部使用 `CodeStatus`（HTTP 级别状态码），业务错误码定义在 `BusinessStatus` 枚举中。
- 业务异常：`throw new BusinessException(BusinessStatus.XXX)` 或 `new BusinessException(code, message)`
- 快速失败：`new BusinessException("message")` 默认 code=500
- 校验异常：`ValidUtil.validate(param)` 抛出 `ValidException`
- 全局兜底：`ExceptionControllerAdvice` 统一拦截所有异常，404→`R.fail(404,...)`, 400→`R.fail(400,...)`, 其余→`R.fail(500,...)`
- 禁止捕获异常后只打印堆栈而不处理或向上抛出。

### 3.4 集合与工具
- 优先使用 `commons-lang3` 的 `StringUtils` 等工具。
- 集合初始化建议指定容量或使用 `Lists.newArrayList()`。

### 3.5 枚举规范
- 所有业务枚举类统一放在 `enums` 包下。
- 所有业务枚举必须实现 `BaseDbEnum` 接口，提供 `getCode()` 和 `getDesc()` 方法。
- 枚举类使用 `@Getter` + `@AllArgsConstructor`，字段声明为 `final`。

```java
@Getter
@AllArgsConstructor
public enum XxxEnum implements BaseDbEnum {
    STATUS_A(10, "状态A"),
    STATUS_B(20, "状态B");

    private final Integer code;
    private final String desc;
}
```

## 4. 数据库规范 (MyBatis Plus)

- 所有实体继承 `BaseEntity`，自动提供：
  - `id`：Long 自增主键（`@TableId(type = IdType.AUTO)`）
  - `createTime` / `updateTime`：由 `MetaObjectHandlerConfig` 自动填充
  - `deleted`：逻辑删除（`@TableLogic`，0-未删除，1-已删除）
- 逻辑删除字段统一命名为 `is_deleted`。
- 优先使用 `LambdaQueryWrapper` 进行条件构造，避免硬编码字段名。
- 日志用 `@Slf4j`，调试日志用 `log.isDebugEnabled()` 守卫。

## 5. 对象转换规范 (MapStruct)

- 转换器统一放在 `converter/` 包，`@Mapper(componentModel = "spring")`。
- 更新操作使用 `@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)` 忽略 null 字段，实现增量更新。

## 6. 接口规范

- 使用 RESTful 风格。
- `POST`: 用于创建或执行复杂操作。
- `PUT`: 用于资源更新。
- `GET`: 用于资源获取。
- `DELETE`: 用于资源删除。
- 接口入参必须进行校验，使用 `ValidUtil.validate(param)` 或 Spring Validation 注解。

## 7. 第三方集成 (Vendor)

- 所有第三方集成（如百度、OCR 等）代码应放在 `vendor` 包下。
- 定义统一的 `Adapter` 或 `Api` 接口，隔离外部系统的变化。
- 配置信息（API Key, Secret）统一从 `application.yaml` 读取。

## 8. 注释规范

- 类、接口、方法必须编写 Javadoc 注释，简述其职能。
- 核心业务逻辑建议编写行内注释。
- TODO 项必须标明负责人和原因。

## 9. 测试规范

- 单元测试放在 `src/test/java` 对应包下。
- 测试类命名: `被测试类名 + Test`。
- 推荐使用 `Mockito` 模拟外部依赖。
- 每一个 Service 逻辑分支都应有对应的测试覆盖。

---
最后更新时间: 2026-03-05
