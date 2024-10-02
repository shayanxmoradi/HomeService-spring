package org.example.homeservice.dto;

import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.entity.Specialist;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
