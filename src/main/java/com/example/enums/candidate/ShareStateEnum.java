package com.example.enums.candidate;

import com.example.enums.BaseDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 候选人分享码状态枚举
 */
@Getter
@AllArgsConstructor
public enum ShareStateEnum implements BaseDbEnum {

    ACTIVE(10, "有效"),
    EXPIRED(20, "已过期"),
    DELIVERED(30, "已投递");

    private final Integer code;
    private final String desc;
}
