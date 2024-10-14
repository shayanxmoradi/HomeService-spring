package org.example.homeservice.dto.updatepassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequst(
        @NotBlank(message = "email is required")
        @Email(message = "you should enter valid email address")
        String email,
        @NotBlank(message = "entering old password required")
        String oldPassword,
        @Size(min = 8, max = 8, message = "The length must be exactly 8 characters.")
        @NotBlank(message = "password is required")

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String newPassword) {
}

