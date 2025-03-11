package com.taild.expresstix.infrastructure.distributed.redisson;

public interface RedissonDistributedService {

    RedissonDistributedLocker getDistributedLock(String lockKey);
}
