package com.education.common.model.online;

import com.education.common.constants.EnumConstants;
import com.education.common.model.AdminUserSession;
import com.education.common.model.FrontUserInfoSession;
import com.education.common.utils.ObjectUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/7 14:51
 */

public class OnlineUser {
    @Setter
    @Getter
    private Integer userId;
    @Setter
    @Getter
    private String sessionId; // 服务器sessionId
    @Setter
    @Getter
    private EnumConstants.PlatformType platformType;

    @Setter
    private AdminUserSession adminUserSession;
    @Setter
    private FrontUserInfoSession frontUserInfoSession;

    public OnlineUser(Integer userId, String sessionId, EnumConstants.PlatformType platformType) {
        this.userId = userId;
        this.platformType = platformType;
        this.sessionId = sessionId;
    }

    public Integer getFrontUserId() {
        if (ObjectUtils.isNotEmpty(frontUserInfoSession)) {
            return frontUserInfoSession.getUserId();
        }
        return null;
    }

    public Integer getAdminUserId() {
        if (ObjectUtils.isNotEmpty(adminUserSession)) {
            return adminUserSession.getUserId();
        }
        return null;
    }
}
