package com.education.service.task;
import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.FrontUserInfoSession;
import com.education.common.model.ModelBeanMap;
import com.education.common.utils.IpUtils;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.RequestUtils;
import com.education.service.system.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异步记录系统日志，提升系统响应速度
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/4/24 20:54
 */
@Component
public class SystemLogTask implements TaskListener {

    @Autowired
    private SystemLogService systemLogService;

    @Override
    public void onMessage(TaskParam taskParam) {
        HttpServletRequest request = (HttpServletRequest) taskParam.get("request");
        String methodName = request.getMethod();
        String contentType = request.getHeader("Content-Type");
        Object params = null;
        if (ObjectUtils.isEmpty(contentType) || !contentType.contains("application/json")) {
            params = request.getParameterMap();
        } else {
            params = RequestUtils.readData(request);
        }
        Map dataMap = new HashMap<>();
        dataMap.put("method", methodName);
        dataMap.put("ip", IpUtils.getAddressIp(request));
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
            dataMap.put("params", sb.toString());
        } else {
            dataMap.put("params", params);
        }
        AdminUserSession adminUserSession = (AdminUserSession) taskParam.get("adminUserSession");
        FrontUserInfoSession frontUserInfoSession = (FrontUserInfoSession) taskParam.get("frontUserInfoSession");
        if (ObjectUtils.isNotEmpty(adminUserSession)) {
            dataMap.put("user_id", adminUserSession.getUserMap().get("id"));
        } else if (ObjectUtils.isNotEmpty(frontUserInfoSession)) {
            dataMap.put("user_id", frontUserInfoSession.getUserInfoMap().get("id"));
        }
        dataMap.put("create_date", new Date());
        dataMap.put("platform_type", EnumConstants.PlatformType.WEB_ADMIN.getValue());
        long startTime = taskParam.getLong("startTime");
        dataMap.put("request_time", (System.currentTimeMillis() - startTime) + "ms");
        dataMap.remove("startTime");
        dataMap.remove("request");
        dataMap.remove("adminUserSession");
        dataMap.remove("frontUserInfoSession");
        systemLogService.save(dataMap);
    }
}
