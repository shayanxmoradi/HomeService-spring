package org.example.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Table(name = Order.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)

public class Order extends BaseEntity<Long> {
    public static final String TABLE_NAME = "orders";

    @PrimaryKeyJoinColumn

    @OneToOne
    private Service choosenService;

    @Column(nullable = false)
    private String orderDescription;

    @Column
    private Double offeredPrice;

    @Column
    @Future
    private Date serviceDate;

    @Column
    private Time serviceTime;


    @OneToOne(cascade = CascadeType.ALL)  // Cascade the persist operation
    @PrimaryKeyJoinColumn
    private Address address;

    @ManyToOne
    private Customer customer ;

    @OneToMany
    private List<Specialist> speclistsWhoOffered;

    @OneToOne
    private Specialist chosenSpecialist;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = true)
    private OrderStatus status;

//
//    @Enumerated(EnumType.STRING)
//    @Column
//    private OrderStatus status=OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS;

    @Override
    public String toString() {
        return "Order{" +
               "choosenService=" + choosenService +
               ", orderDescription='" + orderDescription + '\'' +
               ", offeredPrice=" + offeredPrice +
               ", serviceDate=" + serviceDate +
               ", serviceTime=" + serviceTime +
               ", address=" + address +
               ", status=" + status +
               ", speclistsWhoOffered=" + speclistsWhoOffered +
               ", chosenSpecialist=" + chosenSpecialist +
               '}';
    }

}
