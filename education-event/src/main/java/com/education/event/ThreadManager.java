package com.education.event;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程任务并发处理，提高执行效率
 * @author zengjintao
 * @create 2019/4/11 9:31
 * @since 1.0
 **/
@Slf4j
public class ThreadManager<T> {

    private List<T> data;
    private int executeCount; // 每个线程处理的数据量
    private int threadCount; // 线程个数
    private final CountDownLatch countDownLatch;
    private TaskManager taskManager;
    private SqlSessionTemplate sqlSessionTemplate;

    public ThreadManager(List<T> data, int executeCount, TaskManager taskManager, SqlSessionTemplate sqlSessionTemplate) {
        if (data == null) {
            throw new NullPointerException("data can not be null");
        }
        this.data = data;
        this.executeCount = executeCount;
        this.threadCount = this.createThread();
        this.taskManager = taskManager;
        this.countDownLatch = new CountDownLatch(threadCount);
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    private int createThread () {
        int size = data.size();
        int threadCount = size / executeCount; // 线程数
        if (size % executeCount != 0) {
            threadCount ++;
        }
        return threadCount;
    }

    public List<T> getData() {
        return data;
    }

    public void startThread(Class<? extends BaseTask<T>> taskClazz) throws Exception {
        long start = System.currentTimeMillis();
        List<T> cutList = null;
        for (int i = 0; i < threadCount; i++) {
            if (i == threadCount - 1) {
                cutList = data.subList(executeCount * i, data.size());
            } else {
                cutList = data.subList(executeCount * i, executeCount * (i + 1));
            }
            BaseTask task = taskClazz.newInstance();
            task.setCountDownLatch(this.countDownLatch);
            task.setData(cutList);
            task.setSqlSessionTemplate(sqlSessionTemplate);
            taskManager.execute(task);
        }
        countDownLatch.await();
        log.info("耗时:" + (System.currentTimeMillis() - start)  + "秒");
    }

    public int getThreadCount() {
        return threadCount;
    }
}
