package org.example.homeservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PaymentRequst(
        @NotBlank
        @Length(min = 16, max = 16, message = "should be 16 numbers")
        String creditCardNumber,
        @Future
        @NotBlank
        String expireDate,
        @Length(min = 3, max = 3, message = "should be 3 numbers")
        String cvv,
        @NotBlank
        String captcha
) {
}
