package com.example.pojo.candidate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 候选人分页查询入参
 */
@Data
public class CandidatePageParam {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum;

    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private Integer pageSize;

    /** 候选人姓名，模糊查询，可选 */
    private String name;

    /** 渠道，可选 */
    private String channel;

    /** 申请职位ID，可选 */
    private Long positionId;
}
