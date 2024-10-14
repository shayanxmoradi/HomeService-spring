package org.example.homeservice.dto.review;

public record ReviewRequest(
        Long orderId,
        Integer rating,
        String comment

) {
}

