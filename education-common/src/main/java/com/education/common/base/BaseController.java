package com.education.common.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2020/3/8 11:33
 */
public abstract class BaseController {


    @Autowired
    protected RedisTemplate redisTemplate;
    protected static final Set<String> excelTypes = new HashSet<String>() {
        {
            add("application/x-xls");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/vnd.ms-excel");
            add("text/xml");
        }
    };

}
