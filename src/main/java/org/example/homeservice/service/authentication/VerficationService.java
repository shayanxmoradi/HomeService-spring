package org.example.homeservice.service.authentication;

import org.example.homeservice.domain.authentication.VerificationToken;

public interface VerficationService {
    VerificationToken save(VerificationToken verificationToken);
    VerificationToken findByUserId(Long userId);
    VerificationToken findByToken(String token);
}
