package com.taild.expresstix.infrastructure.persistence.repository.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import com.taild.expresstix.domain.repository.ticket.TicketDetailRepository;
import com.taild.expresstix.infrastructure.persistence.mapper.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class TicketDetailRepositoryImpl implements TicketDetailRepository {

    @Autowired
    private TicketDetailJPAMapper ticketDetailRepository;

    @Override
    public Optional<TicketDetailEntity> findById(Long id) {
        return ticketDetailRepository.findById(id);
    }

    @Override
    public int decreaseTicketStock(Long ticketId, int quantity, Long stockAvailable) {
        int affectedRow = ticketDetailRepository.decreaseTicketStock(ticketId, quantity, stockAvailable);
        //log.info("Decrease stock, row: {}, quantity {}, stock {}", affectedRow, quantity, stockAvailable);
        return affectedRow;
    }

    @Override
    public int decreaseTicketStock(Long ticketId, int quantity) {
        int affectedRow = ticketDetailRepository.decreaseTicketStock(ticketId, quantity);
        //log.info("Decrease stock, row: {}, quantity {}, stock {}", affectedRow, quantity, stockAvailable);
        return affectedRow;
    }

    @Override
    public Long getAvailableStock(Long ticketId) {
        return ticketDetailRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found: " + ticketId))
                .getStockAvailable();
    }

    @Override
    public List<TicketDetailEntity> findAll() {
        return ticketDetailRepository.findAll();
    }
}
