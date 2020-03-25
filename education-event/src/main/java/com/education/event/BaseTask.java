package com.education.event;


import com.education.common.component.SpringBeanManager;
import com.education.common.model.ModelBeanMap;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zengjintao
 * @create 2019/3/29 14:28
 * @since 1.0
 **/
public abstract class BaseTask<T> implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTask.class);

    private List<T> data;
    protected SqlSessionTemplate sqlSessionTemplate;
    protected CountDownLatch countDownLatch;
    private TaskManager taskManager;
    protected final ModelBeanMap modelBeanMap = new ModelBeanMap();

    public BaseTask(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void execute() throws Exception {
        taskManager.execute(this);
    }

    public BaseTask() {
        this.taskManager = SpringBeanManager.getBean(TaskManager.class);
    }

    public BaseTask(SqlSessionTemplate sqlSessionTemplate, List<T> data, CountDownLatch countDownLatch) {
        this.data = data;
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.countDownLatch = countDownLatch;
        this.taskManager = SpringBeanManager.getBean(TaskManager.class);
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public List<T> getData() {
        return data;
    }

    public BaseTask put(String key, Object value) {
        this.modelBeanMap.put(key, value);
        return this;
    }

    public ModelBeanMap getModelBeanMap() {
        return modelBeanMap;
    }
}
