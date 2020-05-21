package com.education.common.lock;

import com.education.common.disabled.RateLimitLock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 限流
 */
public class SemaphoreApiLock extends ApiLock {

    private static final Map<String, Semaphore> semaphoreCache = new ConcurrentHashMap<>();

    public SemaphoreApiLock(RateLimitLock rateLimitLock) {
        super(rateLimitLock);
    }

    @Override
    public boolean tryLock() throws Exception {
        Semaphore semaphore = getOrCreateSemaphore(rateLimitLock.urlKey(), rateLimitLock.limit());
        if (semaphore.tryAcquire(rateLimitLock.sec())) {
            this.locked = true;
        }
        return this.locked;
    }

    @Override
    public void releaseLock() {
        Semaphore semaphore = getOrCreateSemaphore(rateLimitLock.urlKey(), rateLimitLock.limit());
        if (this.locked) {
            semaphore.release(rateLimitLock.sec());
        }
    }
}
