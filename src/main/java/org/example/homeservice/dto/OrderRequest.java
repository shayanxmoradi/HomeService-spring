package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
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
        String orderDescription,
        @Future
        LocalDateTime serviceTime,
        @Positive(message = "should be positive")
        Double offeredPrice) {
}
