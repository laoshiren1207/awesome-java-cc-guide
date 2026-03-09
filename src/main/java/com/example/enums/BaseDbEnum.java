package com.example.enums;

/**
 * 数据库枚举基础接口，所有业务枚举必须实现此接口
 */
public interface BaseDbEnum {

    /**
     * 获取枚举码值
     */
    Integer getCode();

    /**
     * 获取枚举描述
     */
    String getDesc();
}
