package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.OfferRequest;
import org.example.homeservice.dto.OfferResponse;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferResource {
    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(@RequestBody @Validated OfferRequest offer) {
        Optional<OfferResponse> savingResponse = offerService.save(offer);
        return savingResponse
                .map(off-> ResponseEntity.status(HttpStatus.CREATED).body(off))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
