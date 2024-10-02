package org.example.homeservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = Customer.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "customer_type", discriminatorType = DiscriminatorType.STRING)

public class Customer extends BaseUser {
    public static final String TABLE_NAME = "customer";

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Order> orders;

    public void addOrder(Order order) {
        orders.add(order);
    }


}

