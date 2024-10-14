package org.example.homeservice.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Wallet;
import org.example.homeservice.repository.WalletRepo;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepo walletRepo;

    @Override
    public Wallet getByWalletId(Long userId) {
        return walletRepo.findById(userId).orElseThrow(() -> new ValidationException("this Wallet notfound."));
    }

    @Transactional
    @Override
    public Optional<Wallet> addMoneyToWallet(Long walletId, Double money) {
        Wallet byWalletId = getByWalletId(walletId);
        double newAmount = byWalletId.getCreditAmount() + money;
        byWalletId.setCreditAmount(newAmount);
        return newAmount > byWalletId.getCreditAmount() ? Optional.of(byWalletId) : Optional.empty();
    }

    @Override
    public Optional<Wallet> removeMoneyFromWallet(Long walletId, Double money) {
        Wallet byWalletId = getByWalletId(walletId);
        double newAmount = byWalletId.getCreditAmount() - money;
        if (newAmount < 0) {
            throw new ValidationException("not enough credit amount");//fixme watchout
        }
        byWalletId.setCreditAmount(newAmount);
        return newAmount > byWalletId.getCreditAmount() ? Optional.of(byWalletId) : Optional.empty();
    }
}
