package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 候选人信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("candidate")
public class Candidate extends BaseEntity {

    /** 候选人姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 简历URL */
    private String resumeUrl;

    /** 投递时间 */
    private LocalDateTime applyTime;

    /** 候选人APP渠道（如：BOSS、智联、猎聘、拉勾等） */
    private String channel;

    /** 申请职位ID */
    private Long positionId;
}
