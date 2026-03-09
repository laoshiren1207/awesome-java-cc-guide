package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 猎聘工作经历
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinWorkExpVO {

    /** 公司名称 */
    private String compName;

    /** 职位 */
    private String title;

    /** 入职时间 */
    private String workStart;

    /** 离职时间 */
    private String workEnd;

    /** 工作时长 */
    private String workPeriod;
}
