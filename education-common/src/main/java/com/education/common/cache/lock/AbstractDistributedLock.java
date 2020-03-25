package com.education.common.cache.lock;

/**
 * @author zengjintao
 * @version 1.0
 * @create_at 2019/12/26 19:53
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    protected long expireMsecs = 1000 * 60;//60秒expireMsecs 锁持有超时，防止线程在入锁以后，无限的执行下去，让锁无法释放
    protected long timeoutMsecs = 0;// 锁等待超时

    private String lockName;
    private boolean locked = false;


    public AbstractDistributedLock(String lockName) {
        this.lockName = lockName;
    }

    public String getLockName() {
        return lockName;
    }

    @Override
    public boolean getLock() {
        return this.doGetLock();
    }

    abstract boolean doGetLock();

    abstract boolean doRelease();

    @Override
    public void release() {
        this.doRelease();
    }

    @Override
    public boolean isLocked() {
        return this.locked;
    }
}
