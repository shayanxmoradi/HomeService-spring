package org.example.homeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn( nullable = true)
    Order order;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(nullable = true)
    Service service;//todo remove from here?

    @Column(nullable = false)
    private Duration estimatedDuration;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "specialist_id", nullable = true)
    private Specialist specialist;

}
