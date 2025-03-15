package com.taild.expresstix.application.model.cache;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketDetailCache {

    private Long version = 1L;

    private TicketDetailEntity ticketDetail;

}
