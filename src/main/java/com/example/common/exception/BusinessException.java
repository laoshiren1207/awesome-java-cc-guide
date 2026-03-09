package com.example.common.exception;

import lombok.Data;

import java.io.Serial;

@Data
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3034121940056795549L;

    private Integer code;

    public BusinessException() {

    }

    public BusinessException(BusinessStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code , String message) {
        super(message);
        this.code = code;
    }

}