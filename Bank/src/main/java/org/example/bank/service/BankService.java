package org.example.bank.service;

import org.example.bank.domain.Card;

public interface BankService
{
    boolean validateCard(Card card);
    void addCard(Card card);
}
