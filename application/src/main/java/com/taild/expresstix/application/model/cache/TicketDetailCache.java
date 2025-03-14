package com.taild.expresstix.application.model.cache;

import com.taild.expresstix.domain.model.entity.TicketDetailEntity;

public class TicketDetailCache {

    private Long version;

    private TicketDetailEntity ticketDetail;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TicketDetailEntity getTicketDetail() {
        return ticketDetail;
    }

    public void setTicketDetail(TicketDetailEntity ticketDetail) {
        this.ticketDetail = ticketDetail;
    }
}
