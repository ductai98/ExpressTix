package com.taild.expresstix.application.service.ticket.impl;

import com.taild.expresstix.application.service.ticket.TicketDetailAppService;
import com.taild.expresstix.application.service.ticket.cache.TicketDetailCacheService;
import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetailEntity getTicketDetailById(Long ticketId) {
        return ticketDetailCacheService.getTickerDetailCache(ticketId);
    }
}
