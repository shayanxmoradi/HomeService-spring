package org.example.homeservice.dto;

import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.entity.Specialist;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public record OfferResponse(
        Long id,  // Assuming you want to include the ID of the offer in the response
        LocalDate submittedDate,
        LocalTime submittedTime,
        Double suggestedPrice,
        Long order,
        Long serviceId,
        Integer estimatedDays,
        Integer estimatedHours,
        Date proposedStartDate,
        Integer estimatedMinutes,
        Long specialis
) {
}
