package org.example.homeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.example.homeservice.domain.enums.OrderStatus;

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
    @ManyToOne(fetch = FetchType.EAGER)
    private Service choosenService;

    @Column(nullable = false)
    private String orderDescription;

    @Column
    private Double offeredPrice;


    @Future
    @Column
    private LocalDateTime serviceTime;

    @Column
    private LocalDateTime orderStartedAt;//todo on dtos

   @ManyToOne
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer ;

    @OneToMany
//            (fetch = FetchType.LAZY)
    private List<Specialist> speclistsWhoOffered;

    @OneToOne
    private Specialist chosenSpecialist;

    @OneToOne
    private Offer chosenOffer;



//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "status_id", nullable = true)
//    private OrderStatus status;


    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status= org.example.homeservice.domain.enums.OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS;

    @Override
    public String toString() {
        return "Order{" +
               "choosenService=" + choosenService +
               ", orderDescription='" + orderDescription + '\'' +
               ", offeredPrice=" + offeredPrice +
               ", serviceTime=" + serviceTime +
               ", address=" + address +
               ", status=" + status +
               ", speclistsWhoOffered=" + speclistsWhoOffered +
               ", chosenSpecialist=" + chosenSpecialist +
              // getCustomer()+
               '}';
    }

}
