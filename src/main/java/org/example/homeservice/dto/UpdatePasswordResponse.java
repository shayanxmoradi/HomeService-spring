package org.example.homeservice.dto;

public record UpdatePasswordResponse(
        String email,
        String message
) {
}
