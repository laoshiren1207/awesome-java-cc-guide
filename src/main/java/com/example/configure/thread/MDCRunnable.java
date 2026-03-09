package com.example.configure.thread;

import org.slf4j.MDC;

import java.util.Map;

public class MDCRunnable implements Runnable {
    private final Runnable task;
    private final Map<String, String> contextMap;

    public MDCRunnable(Runnable task) {
        this.task = task;
        this.contextMap = MDC.getCopyOfContextMap(); // 获取当前线程的MDC上下文
    }

    @Override
    public void run() {
        if (contextMap != null) {
            MDC.setContextMap(contextMap); // 设置MDC上下文
        }
        try {
            task.run(); // 执行原始任务
        } finally {
            MDC.clear(); // 清理MDC
        }
    }
}
