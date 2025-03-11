package com.taild.expresstix.infrastructure.persistence.mapper;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketDetailJPAMapper extends JpaRepository<TicketDetailEntity, Long> {
}
