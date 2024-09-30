package org.example.homeservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CustomerRequsetDto(
        @Length(min = 2, max = 50,message = "should be at least 2 char and max 50")
        String firstName,
        @Length(min = 2, max = 50,message = "should be at least 2 char and max 50")
        String lastName,
        @NotNull
        @Email
        String email,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "Password must contain a combination of letters and numbers")
        String password) {}
