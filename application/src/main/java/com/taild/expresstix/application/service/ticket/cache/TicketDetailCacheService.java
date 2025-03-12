package com.taild.expresstix.application.service.ticket.cache;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.service.ticket.TicketDetailDomainService;
import com.taild.expresstix.infrastructure.cache.redis.RedisInfraService;
import com.taild.expresstix.infrastructure.distributed.redisson.RedissonDistributedLocker;
import com.taild.expresstix.infrastructure.distributed.redisson.RedissonDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketDetailCacheService {

    private static final String LOCK_KEY_PREFIX = "PRO_LOCK_KEY_ITEM_";
    private static final String CACHE_KEY_PREFIX = "PRO_TICKET:ITEM:";
    private static final int LOCK_WAIT_TIME = 1;
    private static final int LOCK_LEASE_TIME = 5;

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private RedissonDistributedService redissonDistributedService;

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    private TicketDetailEntity getWithLock(Long id) {
        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock(getLockKey(id));
        try {
            if (!locker.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                log.info("Failed to acquire lock for id: {}", id);
                return null;
            }

            return getAndCacheTicketDetail(id);
        } catch (InterruptedException e) {
            log.error("Interrupted while trying to acquire lock for id: {}", id, e);
            Thread.currentThread().interrupt();
            return null;
        } finally {
            locker.unlock();
        }
    }


    public TicketDetailEntity getTicketDetailCache(Long id) {
        log.info("Fetching ticket detail for id: {}", id);

        TicketDetailEntity ticketDetail = getFromCache(id);
        if (ticketDetail != null) {
            return ticketDetail;
        }

        return getWithLock(id);
    }

    private TicketDetailEntity getFromCache(Long id) {
        TicketDetailEntity ticketDetail = redisInfraService.getObject(getCacheKey(id), TicketDetailEntity.class);
        if (ticketDetail != null) {
            log.info("Cache hit for id: {}", id);
        }
        return ticketDetail;
    }

    private TicketDetailEntity getAndCacheTicketDetail(Long id) {
        TicketDetailEntity ticketDetail = getFromCache(id);
        if (ticketDetail != null) {
            return ticketDetail;
        }

        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        log.info("Fetched from database for id: {}", id);

        cacheTicketDetail(id, ticketDetail);
        return ticketDetail;
    }

    private void cacheTicketDetail(Long id, TicketDetailEntity ticketDetail) {
        redisInfraService.setObject(getCacheKey(id), ticketDetail);
        log.info("Cached ticket detail for id: {}", id);
    }

    private String getCacheKey(Long itemId) {
        return CACHE_KEY_PREFIX + itemId;
    }

    private String getLockKey(Long itemId) {
        return LOCK_KEY_PREFIX + itemId;
    }

    private String genEventItemKey(Long itemId) {
        return "PRO_TICKET:ITEM:" + itemId;
    }
}
