package org.example.homeservice.dto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OfferResponse(
        Long id,  // Assuming you want to include the ID of the offer in the response
        LocalDateTime offeredTimeToStart,
        Double suggestedPrice,
        Long orderId,
        Long serviceId,
        LocalDate proposedStartDate,
        Duration estimatedDuration,
        Long specialistId
) {
}
