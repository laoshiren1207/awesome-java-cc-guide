package com.example.configure.web;


import com.example.pojo.R;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@ControllerAdvice
public class ResBodyRequestIdAdvice implements ResponseBodyAdvice<R<?>> {


    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理 ResBody
        return R.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public R<?> beforeBodyWrite(R<?> body,
                                      MethodParameter returnType,
                                      MediaType selectedContentType,
                                      Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      ServerHttpRequest request,
                                      ServerHttpResponse response) {
        if (body != null) {
            // MDC 读取 requestId
            String requestId = MDC.get(RequestIdGenerator.MDC_REQUEST_ID);
            body.setRequestId(requestId);
        }
        return body;
    }
}