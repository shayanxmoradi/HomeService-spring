package org.example.entites;

import jakarta.persistence.*;
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
    private String zip;

}
