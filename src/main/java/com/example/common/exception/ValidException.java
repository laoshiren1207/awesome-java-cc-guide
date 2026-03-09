package com.example.common.exception;

import lombok.Data;

import java.io.Serial;

@Data
public class ValidException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -6802541361775312056L;

    private Integer code;



    public ValidException() {

    }

    public ValidException(BusinessStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public ValidException(String message) {
        super(message);
        this.code = 500;
    }

    public ValidException(Integer code , String message) {
        super(message);
        this.code = code;
    }

}