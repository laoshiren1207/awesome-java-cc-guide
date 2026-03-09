package com.example.pojo.liepin.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 猎聘搜索接口响应 - data 层
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiepinSearchDataVO {

    private LiepinCvSearchResultVO cvSearchResultForm;
}
