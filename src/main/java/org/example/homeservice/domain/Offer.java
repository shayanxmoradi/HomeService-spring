package org.example.homeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

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
    Service service;//todo remove from here?

    @Column(nullable = false)
    private Duration estimatedDuration;


    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

}
