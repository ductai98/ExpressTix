package com.taild.expresstix.infrastructure.distributed.redisson;

import java.util.concurrent.TimeUnit;

public interface RedissonDistributedLocker {

    boolean tryLock(long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    void lock(long leaseTime, TimeUnit timeUnit);

    void unlock();

    boolean isLocked();

    boolean isHeldByThread(long threadId);

    boolean isHeldByCurrentThread();

}
