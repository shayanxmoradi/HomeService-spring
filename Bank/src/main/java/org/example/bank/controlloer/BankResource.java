package org.example.bank.controlloer;

import lombok.RequiredArgsConstructor;
import org.example.bank.domain.Card;
import org.example.bank.service.BankService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankResource {
    private final BankService bankService;

    @PostMapping
    public String saveCard(@Validated @RequestBody Card card) {
        bankService.addCard(card);
        return "saved successfully";
    }
    @PostMapping("valid")
    public String validateCard(@RequestBody Card card) {
        if (bankService.validateCard(card)) {
            return "validated successfully";
        }
        else return "invalid";

    }
}
