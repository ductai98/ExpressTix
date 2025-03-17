package com.taild.expresstix.application.service.ticket;

import com.taild.expresstix.domain.model.dto.TicketDetailDTO;

import java.util.List;


public interface TicketDetailAppService {
    TicketDetailDTO getTicketDetailById(Long ticketId, Long version);
    boolean decreaseTicketStock(Long ticketId, int quantity);
    Long getAvailableStock(Long ticketId);
    List<TicketDetailDTO> getAll();
}
