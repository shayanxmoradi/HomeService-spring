package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.homeservice.domain.Specialist;
import org.example.homeservice.domain.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponse(

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
        Double offeredPrice,
        OrderStatus status,
        Long chosenSpecialistId
) {
}
