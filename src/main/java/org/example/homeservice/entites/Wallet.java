package org.example.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = Wallet.TABLE_NAME)
@Data
public class Wallet extends BaseEntity<Long> {

    public static final String TABLE_NAME = "wallet";

    @Column
    double creditAmount;
}
