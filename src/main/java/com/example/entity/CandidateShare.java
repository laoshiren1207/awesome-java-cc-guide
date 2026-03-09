package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 候选人分享码实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("candidate_share")
public class CandidateShare extends BaseEntity {

    /** 候选人ID */
    private Long candidateId;

    /** 分享码 */
    private String shareCode;

    /** 过期时间 */
    private LocalDateTime expireTime;

    /** 分享状态：10-有效，20-已过期，30-已投递 */
    private Integer shareState;

    /** 状态变更时间 */
    private LocalDateTime stateTime;
}
