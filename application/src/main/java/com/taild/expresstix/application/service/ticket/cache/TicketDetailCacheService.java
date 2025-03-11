package com.taild.expresstix.application.service.ticket.cache;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.service.ticket.TicketDetailDomainService;
import com.taild.expresstix.infrastructure.cache.redis.RedisInfraService;
import com.taild.expresstix.infrastructure.distributed.redisson.RedissonDistributedLocker;
import com.taild.expresstix.infrastructure.distributed.redisson.RedissonDistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TicketDetailCacheService {

    @Autowired
    private RedisInfraService redisInfraService;

    @Autowired
    private RedissonDistributedService redissonDistributedService;

    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    public TicketDetailEntity getTicketDetailById(Long ticketId) {
        TicketDetailEntity ticketDetail = redisInfraService.getObject(genEventItemKey(ticketId), TicketDetailEntity.class);

        if (ticketDetail != null) {
            return ticketDetail;
        }

        RedissonDistributedLocker locker = redissonDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM_" + ticketId);

        try {
            //Must unlock after 5 seconds to avoid lock contention
            boolean isLocked = locker.tryLock(1, 5, TimeUnit.SECONDS);

            // If not locked, return null
            if (!isLocked) {
                return null;
            }

            ticketDetail = redisInfraService.getObject(genEventItemKey(ticketId), TicketDetailEntity.class);

            if (ticketDetail != null) {
                return ticketDetail;
            }

            ticketDetail = ticketDetailDomainService.getTicketDetailById(ticketId);

            // If not found in databases, set cache to null
            if (ticketDetail == null) {
                redisInfraService.setObject(genEventItemKey(ticketId), null);

                return null;
            }

            // Set cache with ticketDetail
            redisInfraService.setObject(genEventItemKey(ticketId), ticketDetail);

            return ticketDetail;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private String genEventItemKey(Long itemId) {
        return "PRO_TICKET:ITEM:" + itemId;
    }
}
