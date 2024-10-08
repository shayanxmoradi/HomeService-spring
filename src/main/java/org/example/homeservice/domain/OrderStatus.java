package org.example.homeservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_status")
@Data
public class OrderStatus extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String statusName;


    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}