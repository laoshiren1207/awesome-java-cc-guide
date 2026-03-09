package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 猎聘候选人信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinCandidateVO {

    /** 标签列表 */
    private List<String> tags;

    /** 简历ID编码 */
    private String resIdEncode;

    /** 性别 */
    private String resSex;

    /** 姓名（脱敏） */
    private String resName;

    /** 简历更新时间 */
    private String resModifytime;

    /** 期望工作城市 */
    private String wantDq;

    /** 期望职位 */
    private String wantJobTitle;

    /** 期望薪资 */
    private String wantSalary;

    /** 教育经历列表 */
    private List<LiepinEduExpVO> eduExpList;

    /** 工作经历列表 */
    private List<LiepinWorkExpVO> workExpList;

    /** 最高学历 */
    private String resEdulevelName;

    /** 所在城市 */
    private String resDqName;

    /** 年龄 */
    private String ageShow;

    /** 工作年限 */
    private String workYearsShow;

    /** 简历详情URL */
    private String resumeUrl;

    /** 活跃状态 */
    private LiepinActiveStatusVO activeStatus;
}
