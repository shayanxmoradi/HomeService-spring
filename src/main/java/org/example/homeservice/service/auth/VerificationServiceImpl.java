package org.example.homeservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.VerificationToken;
import org.example.homeservice.repository.auth.VerficationTokenRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerficationService{
private final VerficationTokenRepo verficationTokenRepo;

    @Override
    public VerificationToken save(VerificationToken verificationToken) {
        return verficationTokenRepo.save(verificationToken);
    }

    @Override
    public VerificationToken findByUserId(Long userId) {
        return verficationTokenRepo.findByUserId(userId);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verficationTokenRepo.findByToken(token);
    }
}
