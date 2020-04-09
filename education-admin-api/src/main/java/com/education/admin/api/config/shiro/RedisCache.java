package com.education.admin.api.config.shiro;

import com.education.common.utils.ObjectUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

/**
 * @descript:
 * @Auther: zengjintao
 * @Date: 2020/3/27 15:32
 * @Version:2.1.0
 */
public class RedisCache <K, V> implements Cache<K, V> {

    private RedisTemplate redisTemplate;
    private ValueOperations valueOperations;
    private int expire;

    public RedisCache(RedisTemplate redisTemplate, int expire) {
        if (expire <= 0) {
            throw new RuntimeException("expire can not be 0");
        }
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.expire = expire;
    }

    @Override
    public V get(K k) throws CacheException {
        return (V) redisTemplate.opsForValue().get(k);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        this.redisTemplate.opsForValue().set(k, v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        V value = (V) this.valueOperations.get(k);
        this.redisTemplate.delete(k);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        this.redisTemplate.delete(this.redisTemplate.keys("*"));
    }

    @Override
    public int size() {
        return this.redisTemplate.keys("*").size();
    }

    @Override
    public Set<K> keys() {
        return this.redisTemplate.keys("*");
    }

    @Override
    public Collection<V> values() {
        Set<K> keys = keys();
        if (ObjectUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }
        Set<V> values = new HashSet<>(keys.size());
        keys.forEach(key -> {
            values.add((V) valueOperations.get(key));
        });
        return values;
    }
}
