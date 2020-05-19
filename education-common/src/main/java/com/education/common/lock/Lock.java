package com.education.common.lock;

import com.education.common.exception.BusinessException;

/**
 * api 限流锁
 */
public interface Lock {

    /**
     * 获取锁
     * @return
     */
    boolean tryLock() throws Exception;

    /**
     * 释放锁
     */
    void releaseLock();
}
