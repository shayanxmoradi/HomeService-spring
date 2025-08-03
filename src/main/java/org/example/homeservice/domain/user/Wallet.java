package org.example.homeservice.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.BaseEntity;

@Entity
@Table(name = Wallet.TABLE_NAME)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Wallet extends BaseEntity<Long> {

    public static final String TABLE_NAME = "wallet";

    @Column
    double creditAmount;
}
