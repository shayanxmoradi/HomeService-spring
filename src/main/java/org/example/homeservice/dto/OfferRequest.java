package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record OfferRequest(
        @Future(message = "chosen date shoul be in future")
        @NotNull(message = "this field is required and must not be null")
        LocalDateTime offeredTimeToStart,
        @NotNull(message = "this field is required and must not be null")
        @Positive
        Double suggestedPrice,
        @NotNull(message = "this field is required and must not be null")
        Long orderId,
        @NotNull(message = "this field cant be null")
        Duration estimatedDuration,
        @NotNull(message = "this field is required and must not be null")
        Long specialistId
) {
}
