package com.education.common.model.online;

import com.education.common.cache.CacheBean;
import com.education.common.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import java.util.Set;

/**
 * 在线用户管理器
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/4/7 14:52
 */
@Component
public final class OnlineUserManager {

    private final CacheBean cacheBean;
    // 用户id 集合
    private static final String USER_ID_CACHE = "user:id:cache:";

    public OnlineUserManager(CacheBean cacheBean) {
        this.cacheBean = cacheBean;
    }

    /**
     * 添加用户
     * @param userId
     * @param onlineUser
     */
    public void addOnlineUser(Integer userId, OnlineUser onlineUser) {
        this.cacheBean.put(USER_ID_CACHE, userId, onlineUser);
    }

    public void removeOnlineUser(Integer userId) {
        this.cacheBean.remove(USER_ID_CACHE, userId);
    }


    public OnlineUser getOnlineUser(Integer userId) {
        return this.cacheBean.get(USER_ID_CACHE, userId);
    }

    /**
     * 删除所有用户
     */
    public void clearOnlineUser() {
        Set<String> userIds = (Set<String>) this.cacheBean.getKeys(USER_ID_CACHE);
        if (ObjectUtils.isNotEmpty(userIds)) {
            userIds.forEach(key -> {
                this.cacheBean.remove(key);
            });
        }
    }
}
