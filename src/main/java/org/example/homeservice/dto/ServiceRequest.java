package org.example.homeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.example.homeservice.dto.validator.ServiceValidator;

import java.util.List;
@ServiceValidator
public record ServiceRequest(

    @NotBlank(message = "Name cannot be blank")
    String name,
    String description,
    @Positive
    Float basePrice,
    Long parentServiceId,
    boolean category,
    List<Long> availableSpecialistsIds){

}
