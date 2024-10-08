package org.example.homeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.hibernate.validator.constraints.Length;

public record SpecialistRequest(
        @NotBlank(message = "firstname is required")
        @Length(min = 2, max = 50,message = "should be at least 2 char and max 50")
        String firstName,
        @NotBlank(message = "lastname is required")
        @Length(min = 2, max = 50,message = "should be at least 2 char and max 50")
        String lastName,
        @Email
        @NotBlank(message = "email is required")
        String email,
        @NotBlank(message = "password is required")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String password,
                                SpecialistStatus specialistStatus,
                                Double rate,
                                byte[] personalImage
) {
}
