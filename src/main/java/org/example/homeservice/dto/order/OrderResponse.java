package org.example.homeservice.dto.order;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
        LocalDateTime orderStartedAt,

        @Positive(message = "should be positive")
        Double offeredPrice,
        OrderStatus status,
        Long chosenSpecialistId
) {
}
