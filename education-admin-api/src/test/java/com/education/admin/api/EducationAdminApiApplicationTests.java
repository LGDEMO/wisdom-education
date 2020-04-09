package com.education.admin.api;


import com.jfinal.weixin.sdk.utils.HttpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class EducationAdminApiApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insertRegion() {
        List<Map<String, Object>> data = jdbcTemplate.queryForList("select * from system_region1");
        data.forEach(item -> {
            jdbcTemplate.update("insert into system_region(code, parent_code, name, full_name, create_date) values(?, ?, ?, ?, ?)",
                    item.get("code"), item.get("parent_code"), item.get("name"), item.get("full_name"), item.get("create_date"));
        });
    }

    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = HttpUtils.get("http://127.0.0.1/system/log/list?name=dsd&age=sdsd");
                    System.err.println(content);
                }
            }).start();
        }
    }

    @Test
    public void testRedis() {
        long expires = System.currentTimeMillis()  + 1;

       // boolean result = redisTemplate.opsForValue().setIfAbsent("TEST", expires);
        System.err.println((Long) redisTemplate.opsForValue().get("TEST"));
      //  redisTemplate.opsForValue().getAndSet("TEST", expires);
    }
}
