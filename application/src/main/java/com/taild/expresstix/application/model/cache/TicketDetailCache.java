package com.taild.expresstix.application.model.cache;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class TicketDetailCache {

    private Long version = 1L;

    private Long id;

    private String name;

    private String description;

    private Long stockInitial;

    private Long stockAvailable;

    private boolean isStockPrepared;

    private Long priceOriginal;

    private Long priceFlash;

    private Date saleStartTime;

    private Date saleEndTime;

    private int status;

    private Long activityId;

    private Date updatedAt;

    private Date createdAt;

}
