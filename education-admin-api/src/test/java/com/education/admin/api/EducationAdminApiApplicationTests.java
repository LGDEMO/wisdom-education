package com.education.admin.api;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EducationAdminApiApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        System.err.println(redisTemplate);
    }
}
