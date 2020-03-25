package com.education.common.cache.lock;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis 分布式锁
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/26 19:49
 */
public class RedisDistributedLock extends AbstractDistributedLock {

    private RedisTemplate redisTemplate;

    public RedisDistributedLock(RedisTemplate redisTemplate, String lockName) {
        super(lockName);
        this.redisTemplate = redisTemplate;
    }

    @Override
    boolean doGetLock() {
        return false;
    }

    @Override
    boolean doRelease() {
        return false;
    }
}
