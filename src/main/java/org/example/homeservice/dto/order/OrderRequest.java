package org.example.homeservice.dto.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record OrderRequest(
        @NotNull
        Long customerId,
        @NotNull
        Long serviceId,
        @NotNull
        Long addressId,
        @NotBlank
        String orderDescription,
        @Future
        @NotNull
        LocalDateTime serviceTime,
        @Positive(message = "should be positive")
        @NotNull
        Double offeredPrice) {
}
