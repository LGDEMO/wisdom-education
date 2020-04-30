package com.education.service.task;

import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.online.OnlineUser;
import com.education.common.model.online.OnlineUserManager;
import com.education.common.utils.IpUtils;
import com.education.service.system.SystemAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异步监听用户登录成功消息推送
 */
@Component
public class UserLoginSuccessListener implements TaskListener {

    @Autowired
    private SystemAdminService systemAdminService;
    @Autowired
    private OnlineUserManager onlineUserManager;

    @Override
    public void onMessage(TaskParam taskParam) {
        AdminUserSession userSession = (AdminUserSession) taskParam.get("userSession");
        HttpServletRequest request = (HttpServletRequest) taskParam.get("request");
        // 更新用户登录信息
        this.updateUserInfo(userSession, request);
        // 添加在线用户
        this.addOnlineUser(userSession);
    }

    private void updateUserInfo(AdminUserSession userSession, HttpServletRequest request) {
        // 更新用户登录信息
        Integer userId = userSession.getUserId();
        Map data = new HashMap<>();
        Integer count = (Integer)userSession.getUserMap().get("login_count");
        data.put("loginCount", count + 1);
        data.put("loginIp", IpUtils.getAddressIp(request));
        data.put("lastLoginTime", new Date());
        data.put("userId", userId);
        systemAdminService.updateByUserId(data);
    }

    private void addOnlineUser(AdminUserSession userSession) {
        // 添加在线用户
        OnlineUser nowOnlineUser = new OnlineUser(userSession.getUserId(),
                userSession.getSessionId(),
                EnumConstants.PlatformType.WEB_ADMIN);
        nowOnlineUser.setAdminUserSession(userSession);
        onlineUserManager.addOnlineUser(userSession.getUserId(), nowOnlineUser);
    }

}
