package com.education.admin.api.config.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2020/3/27 15:03
 * @Version:2.1.0
 */
public class RedisCacheManager implements CacheManager {

    private RedisTemplate redisTemplate;
    // 缓存默认失效时间
    private static final int ONE_HOUR_CACHE = 3600;
    private int expire = ONE_HOUR_CACHE;
    private final Map<String, Cache> caches = new ConcurrentHashMap<>();

    public RedisCacheManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
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
                    cache = new RedisCache<K, V>(redisTemplate, expire);
                }
            }
        }
        return cache;
    }
}
