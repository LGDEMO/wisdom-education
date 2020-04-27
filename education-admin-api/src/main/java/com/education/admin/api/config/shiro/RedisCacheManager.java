package com.education.admin.api.config.shiro;

import com.education.common.cache.CacheBean;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @descript: shiro redis缓存管理器
 * @Auther: zengjintao
 * @Date: 2020/3/27 15:03
 * @Version:2.1.0
 */
public class RedisCacheManager implements CacheManager {

    private CacheBean cacheBean;

    private final Map<String, Cache> caches = new ConcurrentHashMap<>();

    public RedisCacheManager(CacheBean cacheBean) {
        this.cacheBean = cacheBean;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return getCacheFromMap(s);
    }

    private <K, V> Cache<K, V> getCacheFromMap(String s) {
        Cache cache = caches.get(s);
        if (cache == null) {
            synchronized (this) {
                cache = caches.get(s);
                if (cache == null) {
                    cache = new RedisCache<K, V>(cacheBean);
                }
            }
        }
        return cache;
    }
}
