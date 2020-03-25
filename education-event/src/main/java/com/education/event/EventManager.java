package com.education.event;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2020/3/25 09:15
 * @Version:2.1.0
 */
public class EventManager {

    private static final int COUNT = Runtime.getRuntime().availableProcessors(); //cpu个数
    private static final int CORE_SIZE = COUNT * 2;
    private static final int MAX_SIZE = COUNT * 4;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public EventManager() {
        this.threadPoolTaskExecutor = newThreadPoolTaskExecutor();
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    /**
     * 初始化线程池
     * @return
     */
    private ThreadPoolTaskExecutor newThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(MAX_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(CORE_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return threadPoolTaskExecutor;
    }

    public void publishEvent() {

    }
}
