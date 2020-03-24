package com.education.admin.api.config.shiro;

import com.education.mapper.common.cache.EhcacheBean;
import com.education.mapper.common.utils.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2019/12/20 15:15
 * @Version:2.1.0
 */
public class EhcacheShiroSession extends AbstractSessionDAO {

    private static final String sessionKey = "user.cache";
    @Autowired
    private EhcacheBean ehcacheBean;


    public EhcacheShiroSession() {

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
        ehcacheBean.put(sessionKey, id, session);
    }


    @Override
    protected Session doReadSession(Serializable sessionId) {
        return ehcacheBean.get(sessionKey, sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSessionToCache(session.getId(), session);
    }

    @Override
    public void delete(Session session) {
        ehcacheBean.remove(sessionKey, session.getId());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        List<String> keys = ehcacheBean.getKeys(sessionKey);
        List<Session> sessionList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(keys)) {
            keys.forEach(key -> {
                Session session = ehcacheBean.get(sessionKey, key);
                sessionList.add(session);
            });
            return Collections.unmodifiableCollection(sessionList);
        }
        return Collections.emptySet();
    }
}
