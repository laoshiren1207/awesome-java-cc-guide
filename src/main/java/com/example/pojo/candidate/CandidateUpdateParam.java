package com.example.pojo.candidate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 候选人更新入参（全部字段可选，仅传入需要修改的字段）
 */
@Data
public class CandidateUpdateParam {

    private String name;

    private String phone;

    private String resumeUrl;

    private LocalDateTime applyTime;

    private String channel;

    private Long positionId;
}
