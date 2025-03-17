package com.taild.expresstix.infrastructure.persistence.mapper;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TicketDetailJPAMapper extends JpaRepository<TicketDetailEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE TicketDetailEntity td SET td.updatedAt = CURRENT_TIMESTAMP, " +
            "td.stockAvailable = :stockAvailable - :quantity " +
            "WHERE td.id = :ticketId AND td.stockAvailable = :stockAvailable AND td.stockAvailable >= :quantity")
    int decreaseTicketStock(
            @Param("ticketId") Long ticketId,
            @Param("quantity") int quantity,
            @Param("stockAvailable") Long stockAvailable);

    @Modifying
    @Transactional
    @Query("UPDATE TicketDetailEntity td SET td.updatedAt = CURRENT_TIMESTAMP, " +
            "td.stockAvailable = td.stockAvailable - :quantity " +
            "WHERE td.id = :ticketId AND td.stockAvailable >= 0 AND td.stockAvailable >= :quantity")
    int decreaseTicketStock(
            @Param("ticketId") Long ticketId,
            @Param("quantity") int quantity);


}
