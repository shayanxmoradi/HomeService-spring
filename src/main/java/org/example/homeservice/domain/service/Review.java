package org.example.homeservice.domain.service;

import jakarta.persistence.*;
import lombok.Data;
import org.example.homeservice.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = Review.TABLE_NAME)
@Data
public class Review extends BaseEntity<Long> {
    public static final String TABLE_NAME = "review";


    @JoinColumn(nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE})
    Order order;

    @Column(nullable = true)
    String comment;

    @Column(nullable = true)
    int rating;
}
