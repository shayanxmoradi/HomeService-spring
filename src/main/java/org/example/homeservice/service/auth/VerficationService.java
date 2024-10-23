package org.example.homeservice.service.auth;

import org.example.homeservice.domain.VerificationToken;

public interface VerficationService {
    VerificationToken save(VerificationToken verificationToken);
    VerificationToken findByUserId(Long userId);
    VerificationToken findByToken(String token);
}
