package com.education.mapper.common.model.online;

import java.util.HashMap;
import java.util.Map;

/**
 * 在线用户管理器
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/7 14:52
 */
public final class OnlineUserManager {

    private static final Map<Integer, OnlineUser> onlineMap = new HashMap<>();

    public OnlineUserManager() {

    }

    public void addOnlineUser(Integer userId, OnlineUser onlineUser) {
        onlineMap.put(userId, onlineUser);
    }

    public void removeOnlineUser(Integer userId) {
        onlineMap.remove(userId);
    }

    public OnlineUser getOnlineUser(Integer userId) {
        return onlineMap.get(userId);
    }

    public void clearOnlineUser() {
        onlineMap.clear();
    }
}
