package com.taild.expresstix.domain.listener;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.infrastructure.cache.redis.RedisInfraService;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TicketDetailListener {

    private final RedisInfraService redisInfraService;

    @PostUpdate
    public boolean postUpdate(TicketDetailEntity ticketDetailEntity) {

        //clear redis cache
        return redisInfraService.delete("PRO_TICKET:ITEM:" + ticketDetailEntity.getId());
    }
}
