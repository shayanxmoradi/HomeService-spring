package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.service.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceResource {
    private final ServiceService serviceService;

    @GetMapping()
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        System.out.println(":sdf");
        return null;
    }
//todo why this cant be found!
    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@RequestBody @Validated ServiceRequest serviceRequest) {
        System.out.println("hi");
        Optional<ServiceResponse> savingResponse = serviceService.save(serviceRequest);
        return savingResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }}