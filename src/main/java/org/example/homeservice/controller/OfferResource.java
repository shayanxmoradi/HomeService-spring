package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.offer.OfferRequest;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.service.offer.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
public class OfferResource {
    private final OfferService offerService;

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getOfferById(@PathVariable Long id) {
        Optional<OfferResponse> serviceResponse = offerService.findById(id);
        return serviceResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(@RequestBody @Validated OfferRequest offer) {
        Optional<OfferResponse> savingResponse = offerService.save(offer);
        return savingResponse
                .map(off-> ResponseEntity.status(HttpStatus.CREATED).body(off))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    @GetMapping("/offers/{id}")
    public ResponseEntity<List<OfferResponse>> getOffersByOrderId(@PathVariable Long id) {
        List<OfferResponse> offerResponses = offerService.findOfferByOrderId(id);

        if (offerResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(offerResponses);
    }


}
