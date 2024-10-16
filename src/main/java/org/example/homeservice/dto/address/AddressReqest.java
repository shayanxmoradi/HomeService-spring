package org.example.homeservice.dto.address;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.With;
import org.hibernate.validator.constraints.Length;

public record AddressReqest(
        @With
        Long id,
        @NotBlank(message = "this filed is required")
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        String street,
        @NotBlank(message = "this filed is required")
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        String city,
        @NotBlank(message = "this filed is required")
        @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
        String state,
        @NotBlank
        @NotNull
        @Pattern(regexp = "^\\d{5}$", message = "Postal code must be a 5-digit number")
        String zip,
        @NotNull
        Long userId

) {
//        public AddressReqest withId(Long newId) {
//                return new AddressReqest(
//                        newId,
//                        this.street,
//                        this.city,
//                        this.state,
//                        this.zip,
//                        this.userId
//                );
//        }
}
