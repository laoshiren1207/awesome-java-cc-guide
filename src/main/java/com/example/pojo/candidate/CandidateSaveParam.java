package com.example.pojo.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 候选人新增入参
 */
@Data
public class CandidateSaveParam {

    @NotBlank(message = "候选人姓名不能为空")
    private String name;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 简历URL，可选 */
    private String resumeUrl;

    @NotNull(message = "投递时间不能为空")
    private LocalDateTime applyTime;

    @NotBlank(message = "渠道不能为空")
    private String channel;

    @NotNull(message = "申请职位ID不能为空")
    private Long positionId;
}
