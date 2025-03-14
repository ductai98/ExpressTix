package com.taild.expresstix.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.taild.expresstix.application.model.cache.TicketDetailCache;
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

    //local cache
    private static final Cache<Long, TicketDetailCache> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(8)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private RedissonDistributedService redissonDistributedService;

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    private TicketDetailCache getWithLock(Long id, Long version) {
        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock(getLockKey(id));
        try {
            if (!locker.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
                log.info("Failed to acquire lock for id: {}", id);
                return null;
            }

            return getAndCacheTicketDetail(id, version);
        } catch (InterruptedException e) {
            log.error("Interrupted while trying to acquire lock for id: {}", id, e);
            Thread.currentThread().interrupt();
            return null;
        } finally {
            locker.unlock();
        }
    }


    public TicketDetailCache getTickerDetailCache(Long id, Long version) {

        // First, try to get from local cache
        TicketDetailCache ticketDetail = getTicketDetailLocalCache(id);

        if (ticketDetail != null) {
            log.info("Ticket detail found in local cache for id: {}", id);

            if (version == null
                    || ticketDetail.getVersion().equals(version)
                    || ticketDetail.getVersion() > version
            ) {
                return ticketDetail;
            }
        }

        // If not in local cache, try to get from Redis cache
        ticketDetail = getFromRedisCache(id);

        if (ticketDetail != null && ticketDetail.getVersion() >= version) {
            log.info("Ticket detail found in Redis cache for id: {}", id);
            // Update local cache with data from Redis
            ticketDetailLocalCache.put(id, ticketDetail);
            return ticketDetail;
        }

        // If not in Redis cache, fetch from database
        ticketDetail = getWithLock(id, version);

        if (ticketDetail != null) {
            //log.info("Ticket detail fetched from database for id: {}", id);
            // Update local cache
            ticketDetailLocalCache.put(id, ticketDetail);
        }

        return ticketDetail;
    }

    public TicketDetailCache getTicketDetailLocalCache(Long id) {
        try {
            return ticketDetailLocalCache.getIfPresent(id);

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching ticket detail from local cache", e);
        }
    }

    private TicketDetailCache getFromRedisCache(Long id) {
        TicketDetailCache ticketDetail = redisInfraService.getObject(getCacheKey(id), TicketDetailCache.class);
        if (ticketDetail != null) {
            //log.info("Cache hit for id: {}", id);
        }
        return ticketDetail;
    }

    private TicketDetailCache getAndCacheTicketDetail(Long id, Long version) {
        TicketDetailCache ticketDetail = getFromRedisCache(id);
        if (ticketDetail != null && ticketDetail.getVersion() >= version) {
            return ticketDetail;
        }

        //get from database
        TicketDetailEntity ticketDetailEntity = ticketDetailDomainService.getTicketDetailById(id);

        ticketDetail = new TicketDetailCache();
        ticketDetail.setTicketDetail(ticketDetailEntity);
        ticketDetail.setVersion(System.currentTimeMillis());
        log.info("Fetched from database for id: {}", id);

        cacheTicketDetail(id, ticketDetail);
        return ticketDetail;
    }

    private void cacheTicketDetail(Long id, TicketDetailCache ticketDetail) {
        redisInfraService.setObject(getCacheKey(id), ticketDetail);
        //log.info("Cached ticket detail for id: {}", id);
    }

    private String getCacheKey(Long itemId) {
        return CACHE_KEY_PREFIX + itemId;
    }

    private String getLockKey(Long itemId) {
        return LOCK_KEY_PREFIX + itemId;
    }
}
