package com.taild.expresstix.domain.service.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;

public interface TicketDetailDomainService {
    TicketDetailEntity getTicketDetailById(Long ticketId);
}
