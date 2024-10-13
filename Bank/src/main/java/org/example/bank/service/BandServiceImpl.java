package org.example.bank.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.bank.domain.Card;
import org.example.bank.repository.CardRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BandServiceImpl implements BankService {
    private final CardRepository cardRepository;

    @Override
    public boolean validateCard(Card card) {

        Card foundedCard = cardRepository.findByCardNumber(card.getCardNumber()).orElseThrow(() -> new ValidationException("no card with this cardNumber"));
        if (card.equals(foundedCard)) {
            return true;
        }
        return false;
    }

    @Override
    public void addCard(Card card) {
        cardRepository.save(card);
    }
}
