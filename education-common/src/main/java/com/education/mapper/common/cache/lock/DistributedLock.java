package com.education.mapper.common.cache.lock;

/**
 * 分布式锁接口
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/26 19:49
 */
public interface DistributedLock {

    /**
     * 是否获得锁
     * @return
     */
    boolean isLocked();

    /**
     * 释放锁
     */
    void release();

    /**
     * 是否获得锁
     * @return
     */
    boolean getLock();
}
