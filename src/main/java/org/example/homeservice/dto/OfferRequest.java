package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record OfferRequest(
        LocalDate submittedDate,
        LocalTime submittedTime,
        Double suggestedPrice,
        Long orderId,
        Long serviceId,
        Integer estimatedDays,
        Integer estimatedHours,
        @NotNull @Future LocalDate proposedStartDate,
        Integer estimatedMinutes,
        Long specialistId  // Assuming only the ID is needed for the specialist
) {
}
