package com.education.common.cache;


import java.util.List;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/23 21:40
 */
public interface BaseCache {

    <T> T get(String cacheName, Object key);

    <T> T get(Object key);

    void put(String cacheName, Object key, Object value);

    void put(Object key, Object value);

    void put(String cacheName, Object key, Object value, int liveSeconds);

    List getKeys(String cacheName);

    void remove(String cacheName, Object key);

    void removeAll(String cacheName);
}
