package com.taild.expresstix.application.service.ticket.impl;

import com.taild.expresstix.application.model.cache.TicketDetailCache;
import com.taild.expresstix.application.service.ticket.TicketDetailAppService;
import com.taild.expresstix.application.service.ticket.cache.TicketDetailCacheService;
import com.taild.expresstix.domain.model.dto.TicketDetailDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        TicketDetailDTO ticketDetail;
        TicketDetailCache ticketCache = ticketDetailCacheService.getTickerDetailCache(ticketId, version);
        ticketDetail = modelMapper.map(ticketCache.getTicketDetail(), TicketDetailDTO.class);
        ticketDetail.setVersion(ticketCache.getVersion());
        return ticketDetail;
    }
}
