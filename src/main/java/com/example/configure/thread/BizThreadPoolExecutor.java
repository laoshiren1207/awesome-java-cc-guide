package com.example.configure.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Future;


public class BizThreadPoolExecutor  extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        MDCRunnable runnable = new MDCRunnable(task);
        super.execute(runnable);
    }

    @Override
    public Future<?> submit(Runnable task) {
        MDCRunnable runnable = new MDCRunnable(task);
        return super.submit(runnable);
    }
}