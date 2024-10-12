package org.example.homeservice.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Customer;
import org.example.homeservice.dto.*;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerResource {
    private final CustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(
                customerService.findAll().get()
        );
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Validated CustomerRequsetDto customer) {
        Optional<CustomerResponseDto> savedCustomer = customerService.save(customer);
        return savedCustomer
                .map(cust -> ResponseEntity.status(HttpStatus.CREATED).body(cust))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/password")
    public ResponseEntity<UpdatePasswordResponse> updatePass(@RequestBody @Validated UpdatePasswordRequst updatePasswordRequst) {
        customerService.updatePassword(updatePasswordRequst);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse(updatePasswordRequst.email(), "password suffessfully changed for this user.");
        return ResponseEntity.ok(updatePasswordResponse);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<CustomerResponseDto>> filterCustomers(@RequestParam(required = false) String firstName,
                                          @RequestParam(required = false) String lastName,
                                          @Email @Validated @RequestParam(required = false) String email) {
        return ResponseEntity.ok(customerService.filterCustomers(firstName, lastName, email));
    }



}
