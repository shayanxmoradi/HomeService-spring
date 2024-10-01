package org.example.homeservice.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;

public record AddressReqest(


        String street,
        String city,
        String state,
        @Pattern(regexp = "^\\d{5}$", message = "Postal code must be a 5-digit number")
        String zip,
        Long userId

) {
}
