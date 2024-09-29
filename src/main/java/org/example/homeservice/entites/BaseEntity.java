package org.example.homeservice.entites;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@MappedSuperclass
@ToString
@Data
public class BaseEntity<ID extends Serializable> {
    public static final String ID = "id";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ID)
    private ID id;
}
