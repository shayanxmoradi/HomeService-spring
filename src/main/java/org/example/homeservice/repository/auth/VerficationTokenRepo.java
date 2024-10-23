package org.example.homeservice.repository.auth;

import org.example.homeservice.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerficationTokenRepo extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByUserId(Long userId);
    VerificationToken findByToken(String token);
}
