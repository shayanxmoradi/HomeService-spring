package org.example.homeservice.repository.authentication;

import org.example.homeservice.domain.authentication.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerficationTokenRepo extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByUserId(Long userId);

    VerificationToken findByToken(String token);
}
