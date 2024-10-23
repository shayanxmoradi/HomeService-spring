package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.service.adress.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressResource {
    private final AddressService addressService;
    @PreAuthorize("hasAuthority('CUSTOMER')")

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody @Validated AddressReqest address) {
        Optional<AddressResponse> savingResponse = addressService.save(address);
        return savingResponse
                .map(add-> ResponseEntity.status(HttpStatus.CREATED).body(add))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        Optional<AddressResponse> addressResponse = addressService.findById(id);
        return addressResponse
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PreAuthorize("hasAuthority('CUSTOMER') ")

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long id,
                                                         @RequestBody @Validated AddressReqest addressRequest) {

        Optional<AddressResponse> updatedAddressResponse = addressService.update( addressRequest.withId(id));
        return updatedAddressResponse
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @PreAuthorize("hasAuthority('CUSTOMER') ")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        boolean deleted = addressService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
