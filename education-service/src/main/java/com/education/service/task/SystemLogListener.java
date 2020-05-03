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
public class SystemLogListener implements TaskListener {

    @Autowired
    private SystemLogService systemLogService;

    @Override
    public void onMessage(TaskParam taskParam) {
        AdminUserSession adminUserSession = (AdminUserSession) taskParam.get("adminUserSession");
        FrontUserInfoSession frontUserInfoSession = (FrontUserInfoSession) taskParam.get("frontUserInfoSession");
        if (ObjectUtils.isNotEmpty(adminUserSession)) {
            taskParam.put("user_id", adminUserSession.getUserMap().get("id"));
        } else if (ObjectUtils.isNotEmpty(frontUserInfoSession)) {
            taskParam.put("user_id", frontUserInfoSession.getUserInfoMap().get("id"));
        }
        taskParam.put("create_date", new Date());
        taskParam.put("platform_type", EnumConstants.PlatformType.WEB_ADMIN.getValue());
        long startTime = taskParam.getLong("startTime");
        taskParam.put("request_time", (System.currentTimeMillis() - startTime) + "ms");
        taskParam.remove("startTime");
        taskParam.remove("request");
        taskParam.remove("adminUserSession");
        taskParam.remove("frontUserInfoSession");
        systemLogService.save(taskParam);
    }
}
