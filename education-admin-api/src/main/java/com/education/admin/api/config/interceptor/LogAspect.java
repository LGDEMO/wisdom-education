package com.education.admin.api.config.interceptor;

import com.education.mapper.common.annotation.SystemLog;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.RequestUtils;

import com.education.mapper.service1.system.SystemLogService;
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
    @Around("execution(public * com.education.admin.api.controller..*.*(..))")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = RequestUtils.getRequest();

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);
        HttpLogTask httpLogTask = new HttpLogTask(systemLogService, request, systemLogService.getAdminUserSession());
        httpLogTask.put("request_url", RequestUtils.getRequestUrl(request));
        try {
            if (ObjectUtils.isNotEmpty(systemLog)) {
                httpLogTask.setDescribe(systemLog.describe());
            }
            httpLogTask.put("startTime", startTime);
            Object result = pjp.proceed();
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
