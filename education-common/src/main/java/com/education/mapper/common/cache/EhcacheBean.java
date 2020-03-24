package com.education.mapper.common.cache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.List;

/**
 * 基于Ehcache 缓存
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/23 21:11
 */
@Slf4j
public class EhcacheBean implements BaseCache {

    private static final String DEFAULT_CACHE = "default_cache";
    private CacheManager cacheManager;
    private Object rock = new Object();

    public EhcacheBean(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    private Cache getOrAddCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            synchronized (rock) {
                cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    log.warn("Could not find cache config [" + cacheName + "], using default.");
                    cacheManager.addCacheIfAbsent(cacheName);
                    cache = cacheManager.getCache(cacheName);
                    log.debug("Cache [" + cacheName + "] started.");
                }
            }
        }
        return cache;
    }

    public void put(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds) {
        if (liveSeconds <= 0) {
            this.put(cacheName, key, value);
        } else {
            Element element = new Element(key, value);
            element.setTimeToLive(liveSeconds);
            this.getOrAddCache(cacheName).put(element);
        }
    }

    public void put(Object key, Object value) {
        put(DEFAULT_CACHE, key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T)element.getObjectValue() : null;
    }

    public <T> T get(Object key) {
        return get(DEFAULT_CACHE, key);
    }

    @SuppressWarnings("rawtypes")
    public List getKeys(String cacheName) {
        return getOrAddCache(cacheName).getKeys();
    }

    public void remove(String cacheName, Object key) {
        getOrAddCache(cacheName).remove(key);
    }

    public void removeAll(String cacheName) {
        getOrAddCache(cacheName).removeAll();
    }



}
