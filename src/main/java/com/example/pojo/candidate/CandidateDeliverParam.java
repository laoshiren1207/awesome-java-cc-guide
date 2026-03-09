package com.example.pojo.candidate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 候选人投递简历入参
 */
@Data
public class CandidateDeliverParam {

    /** 投递备注 */
    private String remark;

    /** 简历URL */
    private String resumeUrl;

    /** 投递目标职位ID */
    private Long positionId;
}
