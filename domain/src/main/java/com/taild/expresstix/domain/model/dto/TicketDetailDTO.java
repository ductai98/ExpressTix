package com.taild.expresstix.domain.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;


@Data
public class TicketDetailDTO {

    private Long id;

    private String name;

    private String description;

    private Long stockInitial;

    private Long stockAvailable;

    private boolean isStockPrepared;

    private Long price;

    private Date startTime;

    private Date endTime;

    private String startStation;

    private String endStation;

    private int status;

    private Date updatedAt;

    private Date createdAt;

    private Long version;
}
