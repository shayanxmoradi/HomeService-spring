package org.example.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "offer")
@Data
public class Offer extends BaseEntity<Long> {

    @Column
    LocalDateTime submittedDateTime = LocalDateTime.now();//todo carefull this changed

    @Column
            @Future
    LocalDateTime offeredTimeToStart;

    @Column
    Double suggestedPrice;

    @ManyToOne
    Order order;

    @ManyToOne
    Service service;

    @Column(nullable = false)
    private Duration estimatedDuration;


    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

}
