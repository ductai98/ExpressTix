package com.taild.expresstix.domain.repository.ticket;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetailEntity> findById(Long id);
}
