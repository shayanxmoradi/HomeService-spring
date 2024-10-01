package org.example.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "offer")
@Data
public class Offer extends BaseEntity<Long> {

    @Column
    Date submitedDate;

    @Column
    Time submittedTime;

    @Column
    Double suggestedPrice;


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
