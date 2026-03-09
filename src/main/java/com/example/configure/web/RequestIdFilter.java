package com.example.configure.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestIdFilter implements Filter {

    private static final InheritableThreadLocal<Map<String, String>> inheritableMDC = new InheritableThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 生成请求ID并存储在MDC中
            String requestId = RequestIdGenerator.generateRequestId();


            // 将MDC值存储到InheritableThreadLocal中
            Map<String, String> mdcMap = new HashMap<>();
            mdcMap.put(RequestIdGenerator.MDC_REQUEST_ID, requestId);
            inheritableMDC.set(mdcMap);

            // 记录请求ID
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 继续处理请求
            chain.doFilter(request, response);
        } finally {
            // 清理MDC和InheritableThreadLocal
            MDC.clear();
            inheritableMDC.remove();
        }
    }

    @Override
    public void destroy() {
        // 销毁方法
    }
}
