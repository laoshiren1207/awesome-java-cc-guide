package com.example.configure.web;

import org.slf4j.MDC;

import java.util.UUID;


public class RequestIdGenerator {

    public static final String MDC_REQUEST_ID = "requestId";

    public static String generateRequestId() {
        String requestId = UUID.randomUUID().toString();
        MDC.put(MDC_REQUEST_ID, requestId);
        return requestId;
    }

    public static String getMDC(){
        return MDC.get(MDC_REQUEST_ID);
    }
}