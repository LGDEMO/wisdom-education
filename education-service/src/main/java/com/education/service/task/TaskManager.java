package com.education.service.task;

import com.education.common.component.SpringBeanManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 任务管理器
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/4/13 14:37
 */
public class TaskManager {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public TaskManager(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void pushTask(TaskParam taskParam) {
        TaskListener taskListener = SpringBeanManager.getBean(taskParam.getTaskListenerClass());
        if (taskListener != null) {
            threadPoolTaskExecutor.execute(() -> {
                taskListener.onMessage(taskParam);
            });
        }
    }

    public void pushTaskByNewInstance(TaskParam taskParam) {
        Class<? extends TaskListener> taskListenerClass = taskParam.getTaskListenerClass();
        if (taskListenerClass != null) {
            try {
                TaskListener taskListener = taskListenerClass.newInstance();
                threadPoolTaskExecutor.execute(() -> {
                    taskListener.onMessage(taskParam);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
