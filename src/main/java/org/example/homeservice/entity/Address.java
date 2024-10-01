package org.example.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Table(name = Address.TABLE_NAME)
@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Address extends BaseEntity<Long> {
    public static final String TABLE_NAME = "address";
    @Column
    private String street;
    @Column
    private String city;
    @Column
    private String state;
    @Column
    @Pattern(regexp = "^\\d{5}$", message = "Postal code must be a 5-digit number")

    private String zip;
    @ManyToOne(fetch = FetchType.LAZY)
    BaseUser user;
}
