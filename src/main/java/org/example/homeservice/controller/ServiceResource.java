package org.example.homeservice.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.service.admin.AdminService;
import org.example.homeservice.service.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceResource {
    private final ServiceService serviceService;
    private final AdminService adminService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAnyAuthority('SPECIALIST')")

    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(
                serviceService.findAll().get()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        Optional<ServiceResponse> serviceResponse = serviceService.findById(id);
        return serviceResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<ServiceResponse> createService(@RequestBody @Validated ServiceRequest serviceRequest) {
        Optional<ServiceResponse> savingResponse = serviceService.save(serviceRequest);
        return savingResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        ServiceResponse serviceResponse = serviceService.findById(id).
                orElseThrow(() -> new ValidationException("no service with this id is avialbe"));
        boolean deletingResult = serviceService.deleteById(id);
        System.out.println(serviceResponse.name());
        if (serviceResponse == null || !deletingResult) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        //todo why this doenst really remove!

        return deletingResult
                ? ResponseEntity.ok("Successfully deleted")
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //todo is this in right place?
    @PreAuthorize("hasAuthority('ADMIN')")

    @PutMapping("/{serviceId}/add-specialist/{specialistId}")
    public ResponseEntity<String> addSpecialistToService(
            @PathVariable Long serviceId,
            @PathVariable Long specialistId) {
        try {
            adminService.addingSpecialistToSubService(specialistId, serviceId);
            return ResponseEntity.ok("Specialist added to service successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error adding specialist to service: " + e.getMessage());
        }
    }

    @PutMapping("/{serviceId}/delete/{specialistId}")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<String> deleteSpecialistFromService(
            @PathVariable Long serviceId,
            @PathVariable Long specialistId) {
        try {
            adminService.deleteSpecialistFromSubService(specialistId, serviceId);
            return ResponseEntity.ok("Specialist deleted from service successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error adding specialist to service: " + e.getMessage());
        }
    }


}