package org.example.homeservice.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
@Table(name = VerificationToken.TABLE_NAME)
@Entity
@Data
public class VerificationToken extends BaseEntity<Long> {

    public static final String TABLE_NAME = "verification_token";
    private String token;
    @ManyToOne
    private BaseUser user;

    private LocalDateTime expiryDate;
    private Boolean isUsed = false;
}