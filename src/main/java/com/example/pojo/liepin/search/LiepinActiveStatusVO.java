package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 猎聘活跃状态
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinActiveStatusVO {

    /** 状态码：0-在线，1-今天活跃，5-未知 */
    private String code;

    /** 状态描述 */
    private String name;
}
