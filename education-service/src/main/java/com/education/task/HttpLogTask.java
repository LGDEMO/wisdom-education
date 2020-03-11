package com.education.task;

import com.education.common.model.ModelBeanMap;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * http 请求日志任务类
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/1/3 19:16
 */
public class HttpLogTask extends BaseTask<HttpLogTask> {

    public HttpLogTask(SqlSessionTemplate sqlSessionTemplate) {
        super(sqlSessionTemplate);
    }

    @Override
    public void run() {
        ModelBeanMap modelMap = getModelBeanMap();
        String requestUrl = modelMap.getStr("requestUrl");
        put("request_url", requestUrl);
        put("create_date", new Date());
        long startTime = modelMap.getLong("startTime");
        put("request_time", (System.currentTimeMillis() - startTime) + "ms");
        Map dataMap = new HashMap<>();
        modelMap.remove("request");
        modelMap.remove("startTime");
        modelMap.remove("requestUrl");
        dataMap.put("params", modelMap);
        sqlSessionTemplate.insert("http.request.log.saveRequestLog", dataMap);
    }
}
