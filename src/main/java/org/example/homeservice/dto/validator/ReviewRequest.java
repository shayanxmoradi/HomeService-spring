package org.example.homeservice.dto.validator;

public record ReviewRequest(
        Long orderId,
        Integer rating,
        String comment

) {
}

