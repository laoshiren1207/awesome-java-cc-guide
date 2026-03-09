package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 猎聘搜索接口响应（顶层）
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinResponseVO {

    private Integer flag;

    private LiepinSearchDataVO data;
}
