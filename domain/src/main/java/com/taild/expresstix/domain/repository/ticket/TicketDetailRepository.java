package com.taild.expresstix.domain.repository.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetailEntity> findById(Long id);

    int decreaseTicketStock(Long ticketId, int quantity, Long oldStockAvailable);

    int decreaseTicketStock(Long ticketId, int quantity);

    Long getAvailableStock(Long ticketId);

    List<TicketDetailEntity> findAll();
}
