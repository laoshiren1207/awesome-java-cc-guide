# Plan: 邀约与投递记录
> 日期: 2026-03-05
> 关联: [research](./invitation-application-research.md)
> 状态: ✅ 完成

---

## 1. 方案概述

### 1.1 目标
- 新增**邀约（Invitation）**模块：记录招聘者对候选人的邀约行为，绑定候选人+职位，生成分享码
- 新增**投递记录（Application）**模块：候选人通过分享码投递时，生成独立的投递记录，支持一个候选人投递多个职位
- 改造现有 `deliverByShareCode` 逻辑，投递时写入 application 表，同时更新 `Candidate.positionId/applyTime`（保留冗余）

### 1.2 方案选型
- **最终选择**: 邀约与分享码 1:1 关联，邀约表持有 shareId 外键；投递记录独立成表，通过 invitationId 关联邀约来源
    - 理由: 邀约是业务动作，分享码是技术手段，1:1 但职责分离；投递记录独立后支持多职位投递且不破坏现有 candidate 表结构

---

## 2. 实现细节

### 2.1 新增枚举

**InvitationChannelEnum** — 邀约渠道枚举:
```java
BOSS(10, "BOSS直聘"),
ZHILIAN(20, "智联招聘"),
LIEPIN(30, "猎聘");
```

**InvitationStateEnum** — 邀约状态枚举:
```java
PENDING(10, "待响应"),
DELIVERED(20, "已投递"),
EXPIRED(30, "已过期");
```

**ApplicationStateEnum** — 投递状态枚举（当前只有已投递，预留扩展）:
```java
SUBMITTED(10, "已投递");
```

### 2.2 新增表结构

**invitation（邀约表）**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键自增 |
| candidate_id | bigint | 候选人ID |
| position_id | bigint | 职位ID |
| channel | int | 邀约渠道（枚举） |
| share_id | bigint | 关联的分享码ID（1:1） |
| state | int | 邀约状态（待响应/已投递/已过期） |
| state_time | datetime | 状态变更时间 |
| create_time / update_time / deleted | — | BaseEntity 标准字段 |

唯一约束: 无（重复邀约由代码层校验）

**application（投递记录表）**:
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键自增 |
| candidate_id | bigint | 候选人ID |
| position_id | bigint | 职位ID |
| invitation_id | bigint | 来源邀约ID |
| state | int | 投递状态（当前只有已投递） |
| remark | varchar(500) | 投递备注 |
| apply_time | datetime | 投递时间 |
| create_time / update_time / deleted | — | BaseEntity 标准字段 |

### 2.3 新增文件清单

| 文件路径 | 操作类型 | 说明 |
|----------|----------|------|
| `enums/invitation/InvitationChannelEnum.java` | 新增 | 邀约渠道枚举 |
| `enums/invitation/InvitationStateEnum.java` | 新增 | 邀约状态枚举 |
| `enums/application/ApplicationStateEnum.java` | 新增 | 投递状态枚举 |
| `entity/Invitation.java` | 新增 | 邀约实体 |
| `entity/Application.java` | 新增 | 投递记录实体 |
| `mapper/InvitationMapper.java` | 新增 | 邀约 Mapper |
| `mapper/ApplicationMapper.java` | 新增 | 投递记录 Mapper |
| `service/IInvitationService.java` | 新增 | 邀约 Service 接口 |
| `service/impl/InvitationServiceImpl.java` | 新增 | 邀约 Service 实现 |
| `service/IApplicationService.java` | 新增 | 投递记录 Service 接口 |
| `service/impl/ApplicationServiceImpl.java` | 新增 | 投递记录 Service 实现 |
| `pojo/invitation/InvitationSaveParam.java` | 新增 | 邀约创建入参（candidateId, positionId, channel） |
| `pojo/invitation/InvitationVO.java` | 新增 | 邀约 VO |
| `pojo/invitation/InvitationPageParam.java` | 新增 | 邀约分页查询入参 |
| `pojo/application/ApplicationVO.java` | 新增 | 投递记录 VO |
| `pojo/application/ApplicationPageParam.java` | 新增 | 投递记录分页查询入参 |
| `converter/InvitationConverter.java` | 新增 | 邀约对象转换器 |
| `converter/ApplicationConverter.java` | 新增 | 投递记录对象转换器 |
| `facade/invitation/IInvitationAggService.java` | 新增 | 邀约编排接口 |
| `facade/invitation/InvitationAggServiceImpl.java` | 新增 | 邀约编排实现 |
| `controller/invitation/InvitationController.java` | 新增 | 邀约接口 |
| `controller/application/ApplicationController.java` | 新增 | 投递记录查询接口 |
| `db/2026/20260305_invitation_application.sql` | 新增 | 建表 SQL |

### 2.4 修改文件清单

| 文件路径 | 操作类型 | 说明 |
|----------|----------|------|
| `entity/BaseEntity.java` | 修改 | `isDeleted` 改为 `deleted` |
| `db/2026/20260305.sql` | 修改 | 现有表 `is_deleted` 字段改为 `deleted` |
| `facade/candidate/CandidateAggServiceImpl.java` | 修改 | `deliverByShareCode` 改为写入 application 表，更新邀约状态，同时保留 candidate 冗余字段更新 |
| `facade/candidate/ICandidateAggService.java` | 修改 | `deliverByShareCode` 返回值改为 `Long`（返回 applicationId） |
| `controller/candidate/CandidateController.java` | 修改 | 投递接口返回 `R<Long>` |

### 2.5 关键流程

**邀约流程:**
```
1. POST /invitation — 创建邀约
   → 校验候选人存在
   → 校验该候选人+职位未被邀约过（uk 约束 + 代码校验）
   → 调 CandidateShareService.generateShareCode() 生成分享码
   → 创建 Invitation 记录，关联 shareId
   → 返回邀约信息（含分享码）
```

**投递流程（改造后）:**
```
1. POST /candidate/share/{code}/deliver — 候选人通过分享码投递
   → 校验分享码有效（现有逻辑）
   → 通过 shareId 查找关联的 Invitation
   → 创建 Application 记录（candidateId, positionId, invitationId, remark）
   → 更新 Invitation 状态为 DELIVERED
   → 更新 CandidateShare 状态为 DELIVERED（现有逻辑）
   → 更新 Candidate.positionId / applyTime（保留冗余）
```

---

## 3. 内联标注（Annotate 阶段核心）

### 3.1 我的标注
1. invitation 表不使用唯一索引，重复邀约校验在代码层完成
2. BaseEntity 的 `isDeleted` 字段改为 `deleted`，对应数据库字段 `deleted`（不再是 `is_deleted`）

---

## 4. Todo 清单（执行阶段）

- [x] Task 1: 修改 BaseEntity（`isDeleted` → `deleted`）及现有建表 SQL（`is_deleted` → `deleted`）
- [x] Task 2: 创建建表 SQL (`db/2026/20260305_invitation_application.sql`)
- [x] Task 3: 创建枚举类（InvitationChannelEnum、InvitationStateEnum、ApplicationStateEnum）
- [x] Task 4: 创建实体类（Invitation、Application）
- [x] Task 5: 创建 Mapper 接口（InvitationMapper、ApplicationMapper）
- [x] Task 6: 创建 Service 接口和实现（IInvitationService/Impl、IApplicationService/Impl）
- [x] Task 7: 创建 POJO（InvitationSaveParam、InvitationVO、InvitationPageParam、ApplicationVO、ApplicationPageParam）
- [x] Task 8: 创建 Converter（InvitationConverter、ApplicationConverter）
- [x] Task 9: 创建 Facade（IInvitationAggService / InvitationAggServiceImpl）— 邀约创建 + 分享码生成编排
- [x] Task 10: 改造 CandidateAggServiceImpl.deliverByShareCode — 写入 application 表 + 更新邀约状态
- [x] Task 11: 创建 InvitationController（创建邀约、分页查询邀约）
- [x] Task 12: 创建 ApplicationController（分页查询投递记录）
- [x] Task 13: 修改 CandidateController 投递接口返回值
- [x] Task 14: 编译验证 `mvn clean compile` — BUILD SUCCESS

---

## 5. 执行指令（Implement 阶段）

标注完成并批准后，复制以下提示给 Claude 执行：

```txt
implement it all.
mark completed in plan.
do not stop until all done.
no unnecessary comments.
continuously run typecheck...
```

---

## 6. 反馈记录（Feedback 阶段）

* [待记录]

---

## 7. 最终状态

完成时间: [待定]
回滚记录: [无]
遗留问题: [待记录]
