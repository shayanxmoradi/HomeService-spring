package org.example.homeservice.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.homeservice.dto.ApiResponse;
import org.example.homeservice.dto.service.ServiceRequest;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.service.admin.AdminService;
import org.example.homeservice.service.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
@Slf4j
public class ServiceResource {
    private final ServiceService serviceService;
    private final AdminService adminService;


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CUSTOMER') or hasAuthority('ADMIN') or hasAuthority('SPECIALIST')")
    public ResponseEntity<List<ServiceResponse>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll().get());
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
    public ResponseEntity<ApiResponse> deleteService(@PathVariable Long id) {
        serviceService.findById(id)
                .orElseThrow(() -> new ValidationException("No service with ID " + id + " is available."));

        boolean deleted = serviceService.deleteById(id);

        if (deleted) {
            return ResponseEntity.ok(
                    new ApiResponse("Service deleted successfully.", 200, LocalDateTime.now())
            );
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Failed to delete service. Please try again later.", 500, LocalDateTime.now())
            );
        }
    }

    @PutMapping("/{serviceId}/add-specialist/{specialistId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> addSpecialistToService(
            @PathVariable Long serviceId,
            @PathVariable Long specialistId) {
        try {
            adminService.addingSpecialistToSubService(specialistId, serviceId);
            return ResponseEntity.ok(
                    new ApiResponse("Specialist added to service successfully.", 200, LocalDateTime.now())
            );
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Invalid input: " + e.getMessage(), 400, LocalDateTime.now())
            );
        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Invalid input: " +" service with this id "+serviceId +" not found", 400, LocalDateTime.now())
            );
        }
    }

    @PutMapping("/{serviceId}/delete/{specialistId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteSpecialistFromService(
            @PathVariable Long serviceId,
            @PathVariable Long specialistId) {
        try {
            adminService.deleteSpecialistFromSubService(specialistId, serviceId);
            return ResponseEntity.ok(
                    new ApiResponse("Specialist removed from service successfully.", 200, LocalDateTime.now())
            );
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse("Invalid input: " + e.getMessage(), 400, LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("An unexpected error occurred.", 500, LocalDateTime.now())
            );
        }
    }
}