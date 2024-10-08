package org.example.homeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.homeservice.dto.validator.ServiceValidator;
import org.hibernate.validator.constraints.Length;

import java.util.List;
@ServiceValidator
public record ServiceRequest(
@NotNull    @NotBlank(message = "Name cannot be blank")
    @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
    String name,
    @Length(min = 2, max = 50, message = "should be at least 2 char and max 50")
    String description,
    @Positive
    Float basePrice,
    Long parentServiceId,
    @NotNull(message = "cant be blank")
    Boolean isCategory,
    List<Long> availableSpecialistsIds){

}
