package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 猎聘搜索接口响应 - cvSearchResultForm 层
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinCvSearchResultVO {

    /** 候选人列表 */
    private List<LiepinCandidateVO> cvSearchListFormList;

    /** 总数 */
    private Integer totalCount;

    /** 当前页码 */
    private Integer curPage;
}
