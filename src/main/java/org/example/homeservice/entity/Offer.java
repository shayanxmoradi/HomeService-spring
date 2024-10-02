package org.example.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "offer")
@Data
public class Offer extends BaseEntity<Long> {

    @Column
    LocalDate submitedDate;

    @Column
    LocalTime submittedTime;

    @Column
    Double suggestedPrice;

    @ManyToOne
    Order order;

    @ManyToOne
    Service service;


    @Column
    private Integer estimatedDays;

    @Column(nullable = false)
    private Integer estimatedHours;

    @Column(nullable = false)
    @NotNull
    @Future
    private Date proposedStartDate;

    @Column(nullable = false)
    private Integer estimatedMinutes;

    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

}
