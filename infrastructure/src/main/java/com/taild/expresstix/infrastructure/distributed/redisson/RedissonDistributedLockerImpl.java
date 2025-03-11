package com.taild.expresstix.infrastructure.distributed.redisson;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedissonDistributedLockerImpl implements RedissonDistributedService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public RedissonDistributedLocker getDistributedLock(String lockKey) {

        return new RedissonDistributedLocker() {

            RLock rLock = redissonClient.getLock(lockKey);

            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
                boolean isLockSuccess = rLock.tryLock(waitTime, leaseTime, timeUnit);
                log.info("tryLock result: {}", isLockSuccess);
                return isLockSuccess;
            }

            @Override
            public void lock(long leaseTime, TimeUnit timeUnit) {
                rLock.lock(leaseTime, timeUnit);
            }

            @Override
            public void unlock() {
                if (isLocked() && isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }

            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}
