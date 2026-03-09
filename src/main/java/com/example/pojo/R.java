package com.example.pojo;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class R<T> implements Serializable {


    @Serial
    private static final long serialVersionUID = -422553159236648469L;
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回对象
     */
    private T data;

    private String requestId;

    private String timestamp = String.valueOf(System.currentTimeMillis());

    public R() {
        super();
    }

    public R(Integer code) {
        super();
        this.code = code;
    }

    public R(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public R(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.message = throwable.getMessage();
    }

    public R(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public R(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public static <T> R<T> ok(T object) {
        R<T> objectResult = new R<>();
        objectResult.setCode(CodeStatus.OK);
        objectResult.setMessage("操作成功");
        objectResult.setData(object);
        return objectResult;
    }

    public static <T> R<T> ok(String message, T object) {
        R<T> objectResult = new R<>();
        objectResult.setCode(CodeStatus.OK);
        objectResult.setMessage(message);
        objectResult.setData(object);
        return objectResult;
    }

    public static <T> R<T> fail(String message) {
        R<T> objectResult = new R<>();
        objectResult.setCode(CodeStatus.FAIL);
        objectResult.setMessage(message);
        objectResult.setData(null);
        return objectResult;
    }

    public static <T> R<T> fail(String message, T object) {
        R<T> objectResult = new R<>();
        objectResult.setCode(CodeStatus.FAIL);
        objectResult.setMessage(message);
        objectResult.setData(object);
        return objectResult;
    }

    public static <T> R<T> fail(Integer codeStatus, String message) {
        R<T> objectResult = new R<>();
        objectResult.setCode(codeStatus);
        objectResult.setMessage(message);
        objectResult.setData(null);
        return objectResult;
    }

    public static <T> R<T> fail(Integer codeStatus, String message, T object) {
        R<T> objectResult = new R<>();
        objectResult.setCode(codeStatus);
        objectResult.setMessage(message);
        objectResult.setData(object);
        return objectResult;
    }
}