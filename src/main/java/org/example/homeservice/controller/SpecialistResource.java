package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.dto.customer.CustomerRequsetDto;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.SpecialistRequest;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.dto.updatepassword.UpdatePasswordResponse;
import org.example.homeservice.dto.review.SpecialistRateRespone;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/speciallist")
@RequiredArgsConstructor
public class SpecialistResource {
    private final SpeciallistService speciallistService;

    @PostMapping
    public ResponseEntity<SpecialistResponse> createSpecialist(@Validated @RequestBody SpecialistRequest specialistRequest) {
        Optional<SpecialistResponse> specialist = speciallistService.save(specialistRequest);
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
    public ResponseEntity<SpecialistResponse> acceptSpecialist(@PathVariable Long id) {
        Optional<SpecialistResponse> specialist = speciallistService.acceptSpecialist(id);
        return specialist
                .map(specialistResponse -> ResponseEntity.status(HttpStatus.CREATED).body(specialistResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/filter")
    public List<Specialist> filterSpecialists(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String serviceName,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        return speciallistService.filterSpecialists(name, lastName, email, serviceName, "rate", ascending);
    }

    @GetMapping("{id}/rate")
    public Double rateSpecialist(@PathVariable Long id) {
        return speciallistService.showRating(id);
    }

    @GetMapping("{id}/rates")
    public List<SpecialistRateRespone> ratingListSpecialist(@PathVariable Long id) {
        return speciallistService.showReviews(id);
    }
    @GetMapping("/{id}/orders")
    public List<OrderResponse> orderListSpecialist(@PathVariable Long id) {
        return speciallistService.getAvilableOrders(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialist(@PathVariable Long id) {
        if (speciallistService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<SpecialistResponse> updateSpecialist(@PathVariable Long id,
                                                              @RequestBody @Validated SpecialistRequest specialistRequest) {
        Optional<SpecialistResponse> updatedCustomer = speciallistService.update( specialistRequest.withId(id));
        return updatedCustomer
                .map(cust -> ResponseEntity.ok(cust))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
