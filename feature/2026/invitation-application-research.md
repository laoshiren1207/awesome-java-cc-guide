# Research: 邀约与投递记录
> 日期: 2026-03-05

---

## 1. 代码库深读范围

- 涉及模块/文件:
    - `entity/Candidate.java` — 候选人实体，含 `positionId`、`applyTime`、`channel` 等字段
    - `entity/CandidateShare.java` — 分享码实体，含 `shareState`（有效/过期/已投递）
    - `service/ICandidateService.java` + `impl/CandidateServiceImpl.java` — 候选人单实体 CRUD
    - `service/ICandidateShareService.java` + `impl/CandidateShareServiceImpl.java` — 分享码生成/校验/标记投递
    - `facade/candidate/ICandidateAggService.java` + `CandidateAggServiceImpl.java` — 候选人业务编排（含 `deliverByShareCode`）
    - `controller/candidate/CandidateController.java` — 候选人接口（含分享码投递）
    - `pojo/candidate/CandidateDeliverParam.java` — 投递入参（`remark`、`positionId`）
    - `enums/candidate/ShareStateEnum.java` — 分享码状态枚举（ACTIVE/EXPIRED/DELIVERED）
    - `converter/CandidateConverter.java` — MapStruct 转换器
    - `db/2026/20260305.sql` — 现有建表 SQL

- 依赖关系:
    - `CandidateController` → `ICandidateAggService`（Facade）
    - `CandidateAggServiceImpl` → `ICandidateService` + `ICandidateShareService` + `CandidateConverter`
    - `CandidateServiceImpl` → `CandidateMapper`（只访问 candidate 表）
    - `CandidateShareServiceImpl` → `CandidateShareMapper`（只访问 candidate_share 表）

- 已知约束:
    - Service 层只能调用本实体 Mapper，跨实体编排必须在 Facade 层
    - 所有实体继承 `BaseEntity`（id/createTime/updateTime/isDeleted 自动填充）
    - 枚举必须实现 `BaseDbEnum`（code + desc）
    - 接口统一返回 `R<T>`，校验用 `ValidUtil.validate()`
    - 命名约定：Facade 为 `IXxxAggService` / `XxxAggServiceImpl`

---

## 2. 深度分析结果

### 2.1 核心逻辑梳理

- [x] 当前投递流程:
  ```
  1. 招聘者为候选人生成分享码 → POST /candidate/{id}/share
  2. 候选人通过分享码查看信息 → GET /candidate/share/{code}
  3. 候选人通过分享码投递     → POST /candidate/share/{code}/deliver
  ```
  `deliverByShareCode` 直接修改 `Candidate.positionId` 和 `applyTime`，然后调 `candidateShareService.markDelivered()`。

- [x] 问题：
  - **一个候选人只能投一个职位**：`positionId` 在 candidate 表上是单值字段，投递时直接覆盖
  - **没有邀约概念**：分享码只是一个"链接"，不记录谁发起邀约、邀约哪个职位、邀约渠道
  - **投递没有独立记录**：投递结果散落在 candidate 表和 candidate_share 表中，无法追踪投递状态流转
  - **candidate_share 和投递强耦合**：分享码的 DELIVERED 状态既表示"已使用"，又隐含"已投递"

- [x] 关键数据结构:
  - `candidate` 表：id, name, phone, resume_url, apply_time, channel, position_id
  - `candidate_share` 表：id, candidate_id, share_code, expire_time, share_state, state_time

### 2.2 风险点识别

- 潜在破坏点:
  - 现有 `CandidateAggServiceImpl.deliverByShareCode()` 直接操作 `Candidate.positionId/applyTime`，需要改造为写入 application 表
  - `CandidateController` 的投递接口签名和返回值可能需要调整
  - `CandidateDeliverParam` 需要扩展或被新的 Param 替代

- 兼容性问题:
  - `Candidate.positionId` 和 `Candidate.applyTime` 字段后续是否保留？如果投递独立出去，这两个字段应废弃，但需评估是否有其他地方在用
  - 分享码投递接口路径是否保持不变

---

## 3. 待确认问题（已确认）

1. `Candidate.positionId` 和 `Candidate.applyTime` — **保留**，不废弃
2. 邀约时就绑定候选人 + positionId，**邀约即关联职位**
3. 邀约渠道：BOSS、智联招聘、猎聘等，**做成枚举**，只记录类型不做实际发送
4. 投递状态：**暂时不需要流转**，只记录已投递即可
5. 一次邀约只对一个候选人生成一次分享码，**邀约与分享码 1:1**
6. **不允许重复邀约**同一候选人同一职位

---

## 4. 下一步

- 确认所有待确认问题后，进入 `feature/2026/invitation-application-plan.md` 编写阶段
