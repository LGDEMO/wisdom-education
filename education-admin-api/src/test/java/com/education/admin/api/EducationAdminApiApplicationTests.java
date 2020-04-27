package com.education.admin.api;

import com.education.common.cache.EhcacheBean;
import com.education.common.cache.CacheBean;
import com.education.common.utils.IpUtils;
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
    public CacheBean cacheBean;
    static final String cacheName = "user:cache";

    @Test
    public void testRedisCache() {
        cacheBean.put("1", "java");
        cacheBean.put("2", "php");
        cacheBean.put("3", "python");
        String value = cacheBean.get(cacheName, "1");
        System.out.println("value:" + value);
        System.out.println(cacheBean.getKeys(cacheName));
        cacheBean.removeAll(cacheName);
        System.out.println(cacheBean.getKeys(cacheName));
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

    @Test
    public void testIp() {
        System.out.println(IpUtils.getIpAddress("182.101.63.196"));
    }
}
