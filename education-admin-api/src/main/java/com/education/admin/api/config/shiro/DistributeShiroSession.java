package com.education.admin.api.config.shiro;

import com.education.common.cache.CacheBean;
import com.education.common.utils.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式session
 */
public class DistributeShiroSession extends AbstractSessionDAO {

    private final CacheBean cacheBean;
    public static final String SESSION_KEY = "user.session.cache";
    // 缓存默认失效时间
    private static final int WEEK_TIME_OUT = 7 * 24 * 60 * 60;
    private int expire = WEEK_TIME_OUT;

    public DistributeShiroSession(CacheBean cacheBean) {
        this.cacheBean = cacheBean;
    }

    /**
     * 设置缓存失效时间
     * @param expire
     */
    public void setExpire(int expire) {
        this.expire = expire;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        super.assignSessionId(session, sessionId);
        this.saveSessionToCache(sessionId, session);
        return sessionId;
    }

    protected void saveSessionToCache(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        cacheBean.put(SESSION_KEY, id, session, WEEK_TIME_OUT, TimeUnit.SECONDS);
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {
        return cacheBean.get(SESSION_KEY, sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSessionToCache(session.getId(), session);
    }

    @Override
    public void delete(Session session) {
        cacheBean.remove(SESSION_KEY, session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Collection<String> keys = cacheBean.getKeys(SESSION_KEY);
        List<Session> sessionList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(keys)) {
            keys.forEach(key -> {
                Session session = cacheBean.get(SESSION_KEY, key);
                sessionList.add(session);
            });
            return Collections.unmodifiableCollection(sessionList);
        }
        return Collections.emptySet();
    }
}
