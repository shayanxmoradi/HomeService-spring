package org.example.homeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.example.homeservice.domain.enums.OrderStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = Order.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)

public class Order extends BaseEntity<Long> {
    public static final String TABLE_NAME = "orders";

    @PrimaryKeyJoinColumn
//todo check dont fuck oders
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Service choosenService;

    @Column(nullable = false)
    private String orderDescription;

    @Column
    private Double offeredPrice;

    @Column(name = "accepted_price")
    private Double acceptedPrice;


    @Future
    @Column
    private LocalDateTime serviceTime;

    @Column
    private LocalDateTime orderStartedAt;//todo on dtos


    //uncoment this line to delete order it completely
//    @ManyToOne(cascade = {CascadeType.REMOVE}) // Ensure the cascade option is set
//    @JoinColumn(nullable = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @OneToMany
//            (fetch = FetchType.LAZY)
    private List<Specialist> speclistsWhoOffered;

    @OneToOne
    private Specialist chosenSpecialist;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Offer chosenOffer;

//    @OneToOne
//    Review review;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "status_id", nullable = true)
//    private OrderStatus status;


    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status = org.example.homeservice.domain.enums.OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS;

    @Override
    public String toString() {
        return "Order{" +
               "orderDescription='" + orderDescription + '\'' +
               ", offeredPrice=" + offeredPrice +
               ", acceptedPrice=" + acceptedPrice +
               ", serviceTime=" + serviceTime +
               ", orderStartedAt=" + orderStartedAt +
               ", chosenSpecialist=" + chosenSpecialist +
               ", status=" + status +
               '}';
    }
}
