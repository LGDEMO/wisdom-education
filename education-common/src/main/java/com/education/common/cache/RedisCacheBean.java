package com.education.common.cache;

import java.util.List;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/23 21:43
 */
public class RedisCacheBean implements BaseCache {


    @Override
    public <T> T get(String cacheName, Object key) {
        return null;
    }

    @Override
    public void put(String cacheName, Object key, Object value) {

    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds) {

    }

    @Override
    public List getKeys(String cacheName) {
        return null;
    }

    @Override
    public void remove(String cacheName, Object key) {

    }

    @Override
    public void removeAll(String cacheName) {

    }
}
