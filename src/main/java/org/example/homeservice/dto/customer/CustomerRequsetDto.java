package org.example.homeservice.dto.customer;

import jakarta.validation.constraints.*;
import lombok.With;
import org.hibernate.validator.constraints.Length;

public record CustomerRequsetDto(
        @With Long id,
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        @NotBlank(message = "first name is required")
        String firstName,
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        @NotBlank(message = "last name is required")

        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "you should enter valid email address")
        String email,
        @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String password)
{
        public static CustomerRequsetDto updatePassword(CustomerRequsetDto oldDto, String newPassword) {
                return new CustomerRequsetDto(
                        oldDto.id(),
                        oldDto.firstName(),
                        oldDto.lastName(),
                        oldDto.email(),
                        newPassword
                );
        }
}
