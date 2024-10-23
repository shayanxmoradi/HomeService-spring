package org.example.homeservice.service;

import org.example.homeservice.domain.Wallet;

import java.util.Optional;

public interface WalletService {
  Wallet findById(Long walletId);

    Optional<Wallet> addMoneyToWallet(Long WalletId, Double money);
    Optional<Wallet> removeMoneyFromWallet(Long WalletId, Double money);
}
