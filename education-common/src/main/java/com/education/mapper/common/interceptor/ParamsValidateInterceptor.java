package com.education.mapper.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.education.mapper.common.annotation.Param;
import com.education.mapper.common.annotation.ParamsType;
import com.education.mapper.common.annotation.ParamsValidate;
import com.education.mapper.common.utils.ObjectUtils;
import com.education.mapper.common.utils.RegexUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * controller 层参数校验拦截器
 * @author zengjintao
 * @version 1.0
 * @create_at 2018/12/22 19:31
 */
@Component
public class ParamsValidateInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            ParamsValidate paramsValidate = handlerMethod.getMethod().getAnnotation(ParamsValidate.class);
            if (paramsValidate != null) {
                Param[] params = paramsValidate.params();
                ParamsType paramsType = paramsValidate.paramsType();
                return checkParam(request, response, params, paramsType);
            }
        }
        return true;
    }

    private boolean checkParam(HttpServletRequest request, HttpServletResponse response, Param[] params,
                               ParamsType paramsType) {
        Map dataMap = null;
        boolean isJsonData = false;
        if (paramsType == ParamsType.JSON_DATA) {
            String data = readData(request);
            dataMap = JSONObject.parseObject(data);
            isJsonData = true;
        }
        for (Param param : params) {
            String name = param.name();
            Object value = null;
            if (paramsType == ParamsType.FORM_DATA) {
                value = request.getParameter(name);
            } else if (isJsonData && dataMap != null) {
                value = dataMap.get(name);
            }
            Map resultMap = new HashMap<>();
            if (ObjectUtils.isEmpty(value)) {
                resultMap.put("code", param.errorCode());
                resultMap.put("message", param.message());
                renderJson(response, resultMap);
                return false;
            } else {
                String regexp = param.regexp();
                if (ObjectUtils.isNotEmpty(regexp)) {
                    boolean flag = RegexUtils.compile(regexp, value);
                    if (!flag) {
                        resultMap.put("code", param.errorCode());
                        resultMap.put("message", param.regexpMessage());
                        renderJson(response, resultMap);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
