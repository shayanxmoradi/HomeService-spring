package org.example.homeservice.dto.updatepassword;

public record UpdatePasswordResponse(
        String email,
        String message
) {
}
