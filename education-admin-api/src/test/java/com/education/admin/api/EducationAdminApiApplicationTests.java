package com.education.admin.api;

import com.education.common.cache.EhcacheBean;
import com.education.common.cache.CacheBean;
import com.education.common.utils.ObjectUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;


@SpringBootTest
@RunWith(SpringRunner.class)
public class EducationAdminApiApplicationTests {

    @Autowired
    public CacheBean iCache;
    static final String cacheName = "user:cache";

    @Test
    public void testRedisCache() {
        iCache.put("1", "java");
        iCache.put("2", "php");
        iCache.put("3", "python");
        String value = iCache.get(cacheName, "1");
        System.out.println("value:" + value);
        System.out.println(iCache.getKeys(cacheName));
        iCache.removeAll(cacheName);
        System.out.println(iCache.getKeys(cacheName));
    }

    @Test
    public void testEhcache() {
        CacheBean ehcacheBean = new EhcacheBean();
        ehcacheBean.put(cacheName, "1", "java");
        ehcacheBean.put(cacheName, "2", "php");
        ehcacheBean.put(cacheName, "3", "python");
        Collection collection = ehcacheBean.getKeys();
        if (ObjectUtils.isNotEmpty(collection)) {
            collection.forEach(key -> {
                System.out.println("key" + key);
                System.out.println(ehcacheBean.get(key) + "");
            });
        }
    }
}
