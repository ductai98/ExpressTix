package com.taild.expresstix.domain.model.entity;

import com.taild.expresstix.domain.listener.TicketDetailListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ticket_item")
@EntityListeners(TicketDetailListener.class)
public class TicketDetailEntity {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "stock_initial")
    private int stockInitial;

    @Column(name = "stock_available")
    private int stockAvailable;

    @Column(name = "is_stock_prepared")
    private boolean isStockPrepared;

    @Column(name = "price_original")
    private Long priceOriginal;

    @Column(name = "price_flash")
    private Long priceFlash;

    @Column(name = "sale_start_time")
    private Date saleStartTime;

    @Column(name = "sale_end_time")
    private Date saleEndTime;

    @Column(name = "status")
    private int status;

    @Column(name = "activity_id")
    private Long activityId;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_at")
    private Date createdAt;
}
