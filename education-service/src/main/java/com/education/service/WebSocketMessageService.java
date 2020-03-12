package com.education.service;

import com.alibaba.fastjson.JSONObject;
import com.education.common.constants.EnumConstants;
import com.education.common.model.online.OnlineUser;
import com.education.common.model.online.OnlineUserManager;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.task.TaskManager;
import com.education.webSocket.SystemWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/11 14:57
 */
@Service
public class WebSocketMessageService {

    @Autowired
    private OnlineUserManager onlineUserManager;
    @Autowired
    private SystemWebSocketHandler systemWebSocketHandler;
    @Autowired
    private TaskManager taskManager;


    public void checkOnlineUser(Integer userId, EnumConstants.PlatformType platformType) {
        OnlineUser onlineUser = onlineUserManager.getOnlineUser(userId);
        if (ObjectUtils.isNotEmpty(onlineUser)) {
            if (platformType != onlineUser.getPlatformType()) {
                return;
            }
            String sessionId = onlineUser.getSessionId();
            try {
                taskManager.execute(() -> {
                    try {
                        Thread.sleep(10000); // 休眠10秒后在发送消息到前端
                        ResultCode message = new ResultCode(ResultCode.FAIL, "您的账号已在其他地方登录，5秒后将自动下线，" +
                                "如非本人操作请重新登录并及时修改密码");
                        systemWebSocketHandler.sendMessageToPage(sessionId, JSONObject.toJSONString(message));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
