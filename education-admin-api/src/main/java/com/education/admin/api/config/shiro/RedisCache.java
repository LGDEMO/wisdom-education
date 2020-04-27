package com.education.admin.api.config.shiro;
import com.education.common.cache.CacheBean;
import com.education.common.utils.ObjectUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import java.util.*;

/**
 * @descript: shiro RedisCache
 * @Auther: zengjintao
 * @Date: 2020/3/27 15:32
 * @Version:2.1.0
 */
public class RedisCache <K, V> implements Cache<K, V> {

    private CacheBean redisCacheBean;

    public RedisCache(CacheBean redisCacheBean) {
        this.redisCacheBean = redisCacheBean;
    }

    @Override
    public V get(K k) throws CacheException {
        return this.redisCacheBean.get(k);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        this.redisCacheBean.put(k, v);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        V value = (V) this.redisCacheBean.get(k);
        this.redisCacheBean.remove(k);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        this.redisCacheBean.remove();
    }

    @Override
    public int size() {
        return this.keys().size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) this.redisCacheBean.getKeys();
    }

    @Override
    public Collection<V> values() {
        Collection collection = keys();
        if (ObjectUtils.isNotEmpty(collection)) {
            Set<V> values = new HashSet(collection.size());
            collection.forEach(key -> {
                values.add(this.get((K) key));
            });
            return values;
        }
        return Collections.emptySet();
    }
}
