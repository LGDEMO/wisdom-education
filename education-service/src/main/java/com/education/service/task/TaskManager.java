package com.education.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 任务管理器
 * @author zengjintao
 * @create 2019/3/29 14:01
 * @since 1.0
 **/
@Component
public class TaskManager {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void execute(Runnable runnable) {
        threadPoolTaskExecutor.execute(runnable);
    }

    public void shutdown() {
        this.threadPoolTaskExecutor.shutdown();
    }

    public boolean isTerminated() {
        return threadPoolTaskExecutor.getThreadPoolExecutor().isTerminated();
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }
}
