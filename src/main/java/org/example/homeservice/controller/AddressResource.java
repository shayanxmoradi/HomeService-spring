package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.service.adress.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressResource {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody @Validated AddressReqest address) {
        Optional<AddressResponse> savingResponse = addressService.save(address);
        return savingResponse
                .map(add-> ResponseEntity.status(HttpStatus.CREATED).body(add))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }
}
