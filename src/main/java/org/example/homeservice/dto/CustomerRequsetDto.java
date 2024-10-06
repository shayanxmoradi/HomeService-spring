package org.example.homeservice.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record CustomerRequsetDto(
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        @NotBlank(message = "first name is required")
        String firstName,
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        @NotBlank(message = "last name is required")

        String lastName,
        @NotBlank(message = "email is required")
        @Email
        String email,
        @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
        @NotBlank(message = "password is required")

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String password) {
}
