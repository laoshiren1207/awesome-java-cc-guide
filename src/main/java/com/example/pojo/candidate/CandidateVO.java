package com.example.pojo.candidate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 候选人视图对象
 */
@Data
public class CandidateVO {

    private Long id;

    private String name;

    private String phone;

    private String resumeUrl;

    private LocalDateTime applyTime;

    private String channel;

    private Long positionId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
