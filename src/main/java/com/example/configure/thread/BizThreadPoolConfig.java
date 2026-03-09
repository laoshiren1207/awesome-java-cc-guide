package com.example.configure.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BizThreadPoolConfig {


    @Bean(name = "baseTaskExecutor")
    public ThreadPoolTaskExecutor baseTaskExecutor() {
        ThreadPoolTaskExecutor executor = new BizThreadPoolExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        // 设置最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 设置队列容量
        executor.setQueueCapacity(200000);
        // 设置线程前缀
        executor.setThreadNamePrefix("base-task-thread-");
        // 线程池关闭时等待所有任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化线程池
        executor.initialize();
        return executor;
    }

}
