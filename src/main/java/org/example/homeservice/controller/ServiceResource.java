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

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        Optional<ServiceResponse> serviceResponse = serviceService.findById(id);
        return serviceResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    }

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@RequestBody @Validated ServiceRequest serviceRequest) {
        Optional<ServiceResponse> savingResponse = serviceService.save(serviceRequest);
        return savingResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        boolean deletingResult = serviceService.deleteById(id);
        return deletingResult
                ? ResponseEntity.ok("Successfully deleted")
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build();    }
}