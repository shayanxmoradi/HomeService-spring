package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record OfferRequest(
        LocalDateTime offeredTimeToStart,
        Double suggestedPrice,
        Long orderId,
        Long serviceId,
        @NotNull @Future LocalDate proposedStartDate,
        Duration estimatedDuration,
        Long specialistId
) {
}
