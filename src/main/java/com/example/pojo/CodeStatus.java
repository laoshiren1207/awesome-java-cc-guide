package com.example.pojo;

public interface CodeStatus {

    /**
     * 请求成功
     */
    int OK = 200;

    /**
     * 请求失败
     */
    int FAIL = 202;

    /**
     * 熔断请求
     */
    int BREAKING = 204;

    /**
     * 非法请求
     */
    int ILLEGAL_REQUEST = 500;

    /**
     * 非法令牌
     */
    int ILLEGAL_TOKEN = 508;

    /**
     * 其他客户登录
     */
    int OTHER_CLIENTS_LOGGED_IN = 512;

    /**
     * 令牌已过期
     */
    int TOKEN_EXPIRED = 514;

}
