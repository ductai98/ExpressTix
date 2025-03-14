package com.taild.expresstix.application.service.ticket;

import com.taild.expresstix.domain.model.dto.TicketDetailDTO;

public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);
}
