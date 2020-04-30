package com.education.common.cache;

import com.education.common.utils.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


/**
 * 基于 redis 缓存
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/23 21:43
 */
public class RedisCacheBean implements CacheBean {

    private final RedisTemplate redisTemplate;
    private final ValueOperations valueOperations;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public RedisCacheBean(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.redisTemplate.opsForValue();
    }

    @Override
    public <T> T get(String cacheName, Object key) {
        return (T) this.valueOperations.get(this.createNewKey(cacheName, key));
    }

    @Override
    public <T> T get(Object key) {
        return (T) this.valueOperations.get(key);
    }

    @Override
    public void put(String cacheName, Object key, Object value) {
        this.valueOperations.set(this.createNewKey(cacheName, key), value);
    }

    private String createNewKey(String cacheName, Object key) {
       return String.format("%s:%s", cacheName, key);
    }

    @Override
    public void put(Object key, Object value) {
        this.valueOperations.set(key, value);
    }

    @Override
    public void put(Object key, Object value, int liveSeconds) {
        this.valueOperations.set(key, value, liveSeconds);
    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds) {
        this.valueOperations.set(this.createNewKey(cacheName, key), value, liveSeconds);
    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds, TimeUnit timeUnit) {
        this.valueOperations.set(this.createNewKey(cacheName, key), value, liveSeconds, timeUnit);
    }

    @Override
    public void put(Object key, Object value, int liveSeconds, TimeUnit timeUnit) {
        this.valueOperations.set(key, value, liveSeconds, timeUnit);
    }

    @Override
    public Collection getKeys(String cacheName) {
        return this.redisTemplate.keys(cacheName + "*");
    }

    /**
     * 调用此方法需要设置 redisTemplate.setKeySerializer(new StringRedisSerializer());
     * 否则导致返回key 值为空集合
     * @return
     */
    @Override
    public Collection getKeys() {
        return this.redisTemplate.keys("*");
    }

    @Override
    public void remove(Object key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void remove() {
        Collection collection = redisTemplate.keys("*");
        if (ObjectUtils.isNotEmpty(collection)) {
            collection.forEach(key -> {
                redisTemplate.delete(key);
            });
        }
    }

    @Override
    public void remove(String cacheName, Object key) {
        this.redisTemplate.delete(createNewKey(cacheName, key));
    }

    @Override
    public void removeAll(String cacheName) {
        Collection keys = getKeys(cacheName);
        this.redisTemplate.delete(keys);
    }
}
