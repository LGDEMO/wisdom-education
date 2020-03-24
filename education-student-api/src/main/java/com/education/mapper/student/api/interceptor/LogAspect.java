package com.education.mapper.student.api.interceptor;

import com.education.mapper.common.annotation.SystemLog;
import com.education.mapper.common.utils.IpUtils;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.RequestUtils;
import com.education.mapper.service1.system.SystemLogService;
import com.education.service.task.BaseTask;
import com.education.service.task.HttpLogTask;
import com.education.service.task.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志切面
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/1/3 19:32
 */
@Component
@Aspect
@Slf4j
public final class LogAspect {

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SystemLogService systemLogService;


    /**
     * execution表达式示例
     * execution(* com.education.api.controller..*.*(..))
     * 详述：
     * execution()，表达式的主体
     * 第一个“*”符号，表示返回值类型任意；
     * com.sample.service.impl，AOP所切的服务的包名，即我们的业务部分
     * 包名后面的“..”，表示当前包及子包
     * 第二个“*”，表示类名，*即所有类
     * .*(..)，表示任何方法名，括号表示参数，两个点表示任何参数类型
     * 使用异步任务记录 http 请求接口日志
     * @param pjp
     * @return
     */
    @Around("execution(public * com.education.student.api.controller..*.*(..))")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        BaseTask httpLogTask = new HttpLogTask(systemLogService);
        try {
            HttpServletRequest request = RequestUtils.getRequest();
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            SystemLog systemLog = method.getAnnotation(SystemLog.class);
            if (ObjectUtils.isNotEmpty(systemLog)) {
                httpLogTask.put("describe", systemLog.describe());
            }
            httpLogTask.put("request", request);
            httpLogTask.put("requestUrl", RequestUtils.getRequestUrl(request));
            httpLogTask.put("startTime", startTime);
            String methodName = request.getMethod();
            httpLogTask.put("method", method);
            httpLogTask.put("ip", IpUtils.getAddressIp(request));
            Object params = null;
            if ("get".equals(method) || "GET".equals(method)) {
                params = request.getParameterMap();
            } else {
                params = RequestUtils.readData(request);
            }
            if (params instanceof Map) {
                Map<String, Object> dataParam = ((Map) params);
                StringBuilder sb = new StringBuilder("{");
                int i = 0;
                for (String key : dataParam.keySet()) {
                    Object value = dataParam.get(key);
                    if (value instanceof String[]) {
                        String[] arrayValue = (String[]) value;
                        if (i != 0) {
                            sb.append(" ," + key + ": " + arrayValue[0]);
                        } else {
                            sb.append(key + ": " + arrayValue[0]);
                        }
                    }
                    i++;
                }
                sb.append("}");
                httpLogTask.put("params", sb.toString());
            } else {
                httpLogTask.put("params", params);
            }
            httpLogTask.put("user_id", systemLogService.getFrontUserInfoId());
            result = pjp.proceed();
            taskManager.execute(httpLogTask);
            return result;
        } catch (Throwable throwable) {
            StringBuffer error = new StringBuffer();
            error.append(error.toString());
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                error.append(stackTraceElement.toString() + "   ");
            }
            httpLogTask.put("exception", error.toString());
            taskManager.execute(httpLogTask);
            throw throwable;
        }
    }
}
