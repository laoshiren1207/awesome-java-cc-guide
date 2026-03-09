package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 猎聘教育经历
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinEduExpVO {

    /** 专业 */
    private String major;

    /** 学校名称 */
    private String schoolName;

    /** 入学时间 */
    private String eduStart;

    /** 毕业时间 */
    private String eduEnd;

    /** 学历名称 */
    private String eduDegreeName;

    /** 学制时长 */
    private String eduPeriod;
}
