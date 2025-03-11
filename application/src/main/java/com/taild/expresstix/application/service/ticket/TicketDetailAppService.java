package com.taild.expresstix.application.service.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;

public interface TicketDetailAppService {
    TicketDetailEntity getTicketDetailById(Long ticketId);
}
