package org.example.homeservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = Review.TABLE_NAME)
@Data
public class Review extends BaseEntity<Long> {
    public static final String TABLE_NAME = "review";


    @OneToOne()
    Order order;

    @Column(nullable = true)
    String comment;

    @Column(nullable = true)
    int rating;
}
