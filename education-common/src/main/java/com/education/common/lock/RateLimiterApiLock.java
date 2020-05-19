package com.education.common.lock;

import com.education.common.disabled.RateLimitLock;
import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.TimeUnit;

public class RateLimiterApiLock extends ApiLock {

    public RateLimiterApiLock(RateLimitLock rateLimitLock) {
        super(rateLimitLock);
    }

    @Override
    public boolean tryLock() throws Exception {
        RateLimiter rateLimiter = getOrCreateRateLimiter(rateLimitLock.urlKey(), rateLimitLock.limit());
        if (rateLimiter.tryAcquire(rateLimitLock.sec())) {
            this.locked = true;
        }
        return this.locked;
    }

    @Override
    public void releaseLock() {

    }
}
