package com.education.event.impl;

import com.education.event.BaseTask;
import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.IpUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RequestUtils;
import com.education.service.system.SystemLogService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


/**
 * http 请求日志任务类
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/1/3 19:16
 */
public class HttpLogTask extends BaseTask<HttpLogTask> {

    private SystemLogService systemLogService;
    private HttpServletRequest request;
    private String describe;
    private AdminUserSession adminUserSession;

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public HttpLogTask(SystemLogService systemLogService, HttpServletRequest request, AdminUserSession adminUserSession) {
        this.systemLogService = systemLogService;
        this.request = request;
        this.adminUserSession = adminUserSession;
    }

    @Override
    public void run() {
        String methodName = request.getMethod();
        String contentType = request.getHeader("Content-Type");
        Object params = null;
        if (ObjectUtils.isEmpty(contentType) || !contentType.contains("application/json")) {
            params = request.getParameterMap();
        } else {
            params = RequestUtils.readData(request);
        }
        put("method", methodName);
        put("ip", IpUtils.getAddressIp(request));
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
            put("params", sb.toString());
        } else {
            put("params", params);
        }
        put("user_id", adminUserSession.getUserMap().get("id"));
        put("operation_name", adminUserSession.getUserMap().get("login_name"));
        ModelBeanMap modelMap = getModelBeanMap();
        put("create_date", new Date());
        put("platform_type", EnumConstants.PlatformType.WEB_ADMIN.getValue());
        if (ObjectUtils.isNotEmpty(this.describe)) {
            put("operation_desc", adminUserSession.getUserMap().get("login_name") + this.describe);
        }
        long startTime = modelMap.getLong("startTime");
        put("request_time", (System.currentTimeMillis() - startTime) + "ms");
        modelMap.remove("startTime");
        systemLogService.save(modelMap);
    }
}
