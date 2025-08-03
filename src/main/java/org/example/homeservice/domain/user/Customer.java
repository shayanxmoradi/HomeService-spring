package org.example.homeservice.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import org.example.homeservice.domain.enums.UserRole;

@Table(name = Customer.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "customer_type", discriminatorType = DiscriminatorType.STRING)

public class Customer extends BaseUser {
    public static final String TABLE_NAME = "customer";


// making sure no one can change the role
@PrePersist
@PreUpdate
public void enforceCustomerRole() {
    this.userRole = UserRole.CUSTOMER;
}

}

