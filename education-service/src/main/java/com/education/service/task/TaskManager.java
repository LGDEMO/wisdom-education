package com.education.service.task;

import com.education.common.component.SpringBeanManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务管理器
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/4/13 14:37
 */
public class TaskManager {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Map<String, TaskListener> taskListenerMap = new ConcurrentHashMap<>();

    public TaskManager(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void pushTask(TaskParam taskParam) {
        TaskListener taskListener = SpringBeanManager.getBean(taskParam.getTaskListenerClass()); //taskListenerMap.get(beanName);
        if (taskListener != null) {
            threadPoolTaskExecutor.execute(() -> {
                taskListener.onMessage(taskParam);
            });
        }
    }
}
