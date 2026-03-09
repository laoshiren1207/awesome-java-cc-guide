package com.example.configure.advice;

import com.example.pojo.R;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public R<String> error(Exception e){
        log.info("e", e);
        if(e instanceof NoHandlerFoundException exception)  //这里就大概处理一下404就行
            return R.fail(404, e.getMessage());
        else if (e instanceof ServletException exception)  //其他的Servlet异常就返回400状态码
            return R.fail(400, e.getMessage());
        else
            return R.fail(500, e.getMessage());  //其他异常直接返回500
    }
}
