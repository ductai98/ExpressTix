package com.taild.expresstix.domain.service.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.repository.ticket.TicketDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {

    @Autowired
    private TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetailEntity getTicketDetailById(Long ticketId) {
        return ticketDetailRepository.findById(ticketId).orElse(null);
    }
}
