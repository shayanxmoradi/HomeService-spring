package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.specialist.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.dto.updatepassword.UpdatePasswordResponse;
import org.example.homeservice.dto.review.SpecialistRateRespone;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/speciallist")
@RequiredArgsConstructor
public class SpecialistResource {
    private final SpeciallistService speciallistService;

    @PostMapping
    public ResponseEntity<SpecialistResponse> createSpecialist(@Validated @RequestBody SpecialistRequest specialistRequest) {
        Optional<SpecialistResponse> specialist = speciallistService.save(specialistRequest.withSpecialistStatus(SpecialistStatus.PENDING));
        return specialist
                .map(specialistResponse -> ResponseEntity.status(HttpStatus.CREATED).body(specialistResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/password")
    public ResponseEntity<UpdatePasswordResponse> updatePass(@RequestBody @Validated UpdatePasswordRequst updatePasswordRequst) {
        speciallistService.updatePassword(updatePasswordRequst);
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse(updatePasswordRequst.email(), "password suffessfully changed for this user.");
        return ResponseEntity.ok(updatePasswordResponse);
    }

    @PutMapping("/accept/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SpecialistResponse> acceptSpecialist(@PathVariable Long id) {
        Optional<SpecialistResponse> specialist = speciallistService.acceptSpecialist(id);
        return specialist
                .map(specialistResponse -> ResponseEntity.status(HttpStatus.CREATED).body(specialistResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('ADMIN')")

    public List<Specialist> filterSpecialists(
            @RequestParam(required = false) Long numberOfOffers,
            @RequestParam(required = false) Long numberOfOrders,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String serviceName,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        return speciallistService.filterSpecialists(name, lastName, email, serviceName, "rate", ascending,startDate,endDate,numberOfOrders,numberOfOffers);
    }

    @GetMapping("{id}/rate")
    @PreAuthorize("hasAuthority('ADMIN')")

    public Double rateSpecialist(@PathVariable Long id) {
        return speciallistService.showRating(id);
    }

    @GetMapping("{id}/rates")
    @PreAuthorize("hasAuthority('CUSTOMER')")

    public List<SpecialistRateRespone> ratingListSpecialist(@PathVariable Long id) {
        return speciallistService.showReviews(id);
    }
    @GetMapping("/{id}/orders")
    @PreAuthorize("hasAuthority('SPECIALIST')")

    public List<OrderResponse> orderListSpecialist(@PathVariable Long id) {
        return speciallistService.getAvilableOrders(id);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<Void> deleteSpecialist(@PathVariable Long id) {
        if (speciallistService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<SpecialistResponse> updateSpecialist(@PathVariable Long id,
                                                              @RequestBody @Validated SpecialistRequest specialistRequest) {
        Optional<SpecialistResponse> updatedCustomer = speciallistService.update( specialistRequest.withId(id));
        return updatedCustomer
                .map(cust -> ResponseEntity.ok(cust))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
