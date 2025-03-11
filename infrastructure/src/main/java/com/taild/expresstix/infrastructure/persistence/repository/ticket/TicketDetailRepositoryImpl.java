package com.taild.expresstix.infrastructure.persistence.repository.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.repository.ticket.TicketDetailRepository;
import com.taild.expresstix.infrastructure.persistence.mapper.TicketDetailJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketDetailRepositoryImpl implements TicketDetailRepository {

    @Autowired
    private TicketDetailJPAMapper ticketDetailRepository;

    @Override
    public Optional<TicketDetailEntity> findById(Long id) {
        return ticketDetailRepository.findById(id);
    }
}
