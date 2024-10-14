package org.example.homeservice.dto.address;

import jakarta.validation.constraints.Pattern;

public record AddressResponse(

        String street,
        String city,
        String state,
        @Pattern(regexp = "^\\d{5}$", message = "Postal code must be a 5-digit number")
        String zip,
        Long userId
) {
}
